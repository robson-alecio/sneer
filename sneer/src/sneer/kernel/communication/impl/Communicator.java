//Implement Tests:
//Entrando - PK Novo - Rejeitado
//Entrando - PK Novo - Aceito - Nick Novo (Happy Day)
//Entrando - PK Novo - Aceito - Nick Existente - Sem PK (Happy Day)
//Entrando - PK Novo - Aceito - Nick Existente - Com PK: Repete ateh escolher nick novo ou nick sem PK.
//Entrando - PK Existente (Happy Day)
//Entrando - PK Igual a minha propria (localhost) - Rejeita conexao
//Saindo - Veio PK Igual de Outro Contato: Sinalizar com um status de erro no contato.
//Saindo - Veio PK do Proprio Contato (Happy Day)
//Saindo - Veio PK Nova - Contato N tinha PK: persiste PK (Happy Day)
//Saindo - Veio PK Nova - Contato Já Tinha PK Diferente (Contato pode ter reinstalado o Sneer, por exemplo): Mostra warning e seta status do contato p unconfirmed.


package sneer.kernel.communication.impl;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import sneer.kernel.business.Business;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactInfo;
import sneer.kernel.business.contacts.ContactPublicKeyInfo;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import wheel.io.network.ObjectSocket;
import wheel.io.network.OldNetwork;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;
import static wheel.i18n.Language.*;

public class Communicator {

	public Communicator(User user, OldNetwork network, BusinessSource businessSource) {
		_user = user;
		_businessSource = businessSource;
		Business business = businessSource.output();
		
		prepareBusiness();
		
		_spider = new Spider(network, business.contacts(), businessSource.contactOnlineSetter(), outgoingConnectionValidator(), myObjectReceiver());
		new SocketAccepter(user, network, business.sneerPort(), mySocketServer());
	}

	private Omnivore<Object> myObjectReceiver() {
		return new Omnivore<Object>() { public void consume(Object received) {
			if (!(received instanceof ChannelPacket)) return;
			receive((ChannelPacket)received);
		}};
	}

	private void receive(ChannelPacket receivedPacket) {
		ChannelImpl channel = _channelsById.get(receivedPacket._channelId);
		channel.receive(receivedPacket._packet);
	}

	private Consumer<OutgoingConnectionAttempt> outgoingConnectionValidator() {
		return new Consumer<OutgoingConnectionAttempt>() { public void consume(OutgoingConnectionAttempt attempt) throws IllegalParameter {
			ObjectSocket socket = attempt._outgoingSocket;
			String remotePK;
			try {
				socket.writeObject(ownPublicKey().currentValue());
				socket.writeObject(ownName().currentValue());
				remotePK = (String)socket.readObject();
			} catch (IOException e) {
				e.printStackTrace();
				throw new IllegalParameter("");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new IllegalParameter("");
			}

			String contactsPK = attempt._contact.publicKey().currentValue();
			if (remotePK.equals(contactsPK)) return;

			String nick = attempt._contact.nick().currentValue();
			
			Contact thirdParty = findContactGivenPublicKey(remotePK);
			if (thirdParty != null) {
				handleDuplicatePK(nick, thirdParty);
				throw new IllegalParameter(translate("Remote contact has same public key as another contact."));
			}
			
			if (!contactsPK.isEmpty()) notifyUserOfPKMismatch(nick);
			
			_businessSource.contactPublicKeyUpdater().consume(new ContactPublicKeyInfo(nick, remotePK));
		} };

	}

	private void handleDuplicatePK(String nick, Contact thirdParty) {
		_user.acknowledgeNotification(translate("%1$s has the same public key as %2$s. You must delete one of them.",nick,thirdParty.nick().currentValue())); //Fix: update error state for the contact. 
	}

	private void notifyUserOfPKMismatch(String nick) {
		 //Fix: Security implementation: Revert the status of the contact to "unconfirmed" or something of the sort, so that the user has to confirm the remote PK again.
		String notification = translate(
			"SECURITY ALERT FOR CONTACT: %1$s\n\n" +
			"Either this contact has changed its public key or\n" +
			"someone else is trying to trick you and impersonate it.\n\n" +
			"This contact's status will be changed to 'UNCONFIRMED',\n" +
			"so that you can confirm its public key again.", nick);
		_user.acknowledgeNotification(notification);
	}

	private Signal<String> ownPublicKey() {
		return _businessSource.output().publicKey();
	}

	private Signal<String> ownName() {
		return _businessSource.output().ownName();
	}

	private final BusinessSource _businessSource;
	private final User _user;
	private Spider _spider;
	private Map<String, ChannelImpl> _channelsById = new HashMap<String, ChannelImpl>();

	
	private void prepareBusiness() {
		int sneerPort = _businessSource.output().sneerPort().currentValue();
		if (sneerPort == 0) initSneerPort(_businessSource);

		String ownPublicKey = _businessSource.output().publicKey().currentValue();
		if (ownPublicKey.isEmpty()) initPublicKey(_businessSource);
	}


	public Channel getChannel(String channelId, int priority) {
		ChannelImpl result = _channelsById.get(channelId);
		if (result != null) return result;
		
		result = new ChannelImpl(outputFor(channelId, priority));
		_channelsById.put(channelId, result);
		return result;
	}

	private Omnivore<Packet> outputFor(final String channelId, final int priority) {
		return new Omnivore<Packet>() { public void consume(Packet packet) {
			ConnectionImpl connection = _spider.connectionFor(packet._contactId);
			connection.send(new ChannelPacket(channelId, packet), priority);
		}};
	}

	private void initPublicKey(BusinessSource businessSource) {
		String ownPK = "" + System.currentTimeMillis() + "/" + System.nanoTime();
		businessSource.publicKeySetter().consume(ownPK);
	}

	private void initSneerPort(BusinessSource businessSource) {
		int randomPort = 10000 + new Random().nextInt(50000);
		try {
			businessSource.sneerPortSetter().consume(randomPort);
		} catch (IllegalParameter e) {
			throw new IllegalStateException();
		}
	}

	private Omnivore<ObjectSocket> mySocketServer() {
		return new Omnivore<ObjectSocket>() { public void consume(ObjectSocket socket) {
			serve(socket);
		} };
	}

	private void serve(ObjectSocket socket) {
		if (tryToServe(socket)) return;
		
		try {
			socket.close();
		} catch (IOException e) {}
	};

	private boolean tryToServe(final ObjectSocket socket) {
		String publicKey;
		String name;
		try {
			publicKey = (String)socket.readObject();
			name = (String)socket.readObject();
		} catch (IOException ignored) {
			ignored.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		if (ownPublicKey().currentValue().equals(publicKey)) return false;
		
		Contact contact = findContactGivenPublicKey(publicKey);
		
		try {
			if (contact == null) contact = produceContactWithNewPublicKey(name, publicKey);
		} catch (CancelledByUser e) {
			return false;
		}

		try {
			socket.writeObject(ownPublicKey().currentValue());
		} catch (IOException ignored) {
			return false;
		}
		
		_spider.connectionFor(contact.id()).serveIncomingSocket(socket);
		return true;
	}


	private Contact produceContactWithNewPublicKey(String name, String publicKey) throws CancelledByUser {
		String prompt = translate(
				"Someone claiming to be\n\n%1$s\n\n is trying to connect to you. Do you want\n" +
				"to accept the connection?",name);
		if (!_user.confirm(prompt)) throw new CancelledByUser();

		String nick;
		Contact existing;
		while (true) {
			nick = _user.answer(translate("Enter a nickname for your new contact:"), name);
			
			existing = findContactGivenNick(nick);
			if (existing == null) return createContact(publicKey, nick);
			
			if (existing.publicKey().currentValue().isEmpty()) break;
			_user.acknowledgeNotification(translate("There already is another contact with this nickname:\n\n%1$s",nick), translate("Choose Another..."));
		}
		
		_businessSource.contactPublicKeyUpdater().consume(new ContactPublicKeyInfo(nick, publicKey)); //Refactor: Use contactId instead of nick;
		
		return existing;
	}


	private Contact createContact(String publicKey, String nick) throws CancelledByUser {
		try {
			_businessSource.contactAdder().consume(new ContactInfo(nick, "", 0, publicKey, Contact.CONFIRMED_STATE)); //Implement: get actual host addresses from contact.
			return findContactGivenNick(nick);
		} catch (IllegalParameter e) {
			_user.acknowledge(e);
			throw new CancelledByUser();
		}
	}


	private Contact findContactGivenNick(String nick) {
		for (Contact contact : _businessSource.output().contacts())
			if (nick.equals(contact.nick().currentValue())) return contact;
		return null;
	}


	private Contact findContactGivenPublicKey(String publicKey) {
		for (Contact contact : _businessSource.output().contacts())
			if (publicKey.equals(contact.publicKey().currentValue())) return contact;
		return null;
	}

}
