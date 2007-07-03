//Entrando - PK Novo - Rejeitado (HD)
//Entrando - PK Novo - Aceito - Nick Novo (HD)
//Entrando - PK Novo - Aceito - Nick Existente - Sem PK (HD)
//Entrando - PK Novo - Aceito - Nick Existente - Com PK: Repete ateh escolher nick novo ou nick sem PK.
//Entrando - PK Existente (HD)
//Saindo - Sem PK - Veio Novo (HD)
//Saindo - Sem PK - Veio De Outro Contato (HD): Deleta o contato que originou (está sem pk mesmo)
//Saindo - Com PK - Bateu (HD)
//Saindo - Com PK - Nao Bateu - Veio Novo
//Saindo - Com PK - Nao Bateu - Veio De Outro Contato


package sneer.kernel.communication.impl;


import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import sneer.kernel.business.Business;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.ContactInfo;
import sneer.kernel.business.contacts.ContactPublicKeyInfo;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.impl.ChannelImpl.MuxProvider;
import wheel.io.Connection;
import wheel.io.network.ObjectSocket;
import wheel.io.network.OldNetwork;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.exceptions.FriendlyException;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;

public class Communicator {

	public Communicator(User user, OldNetwork network, BusinessSource businessSource) {
		_user = user;
		_businessSource = businessSource;
		Business business = businessSource.output();
		
		prepareBusiness();
		
		new SocketAccepter(user, network, business.sneerPort(), mySocketServer());
		_spider = new Spider(network, business.contacts(), businessSource.contactOnlineSetter(), outgoingConnectionValidator());
	}

	private Consumer<OutgoingConnectionAttempt> outgoingConnectionValidator() {
		return new Consumer<OutgoingConnectionAttempt>() { public void consume(OutgoingConnectionAttempt attempt) throws IllegalParameter {
			ObjectSocket socket = attempt._outgoingSocket;
			String remotePK;
			try {
				socket.writeObject(ownPublicKey().currentValue());
				socket.writeObject(ownName().currentValue());
				remotePK = (String)socket.readObject();
			} catch (Exception e) {
				throw new IllegalParameter("Socket threw IOException");
			}

			String contactsPK = attempt._contact.publicKey().currentValue();
			if (remotePK.equals(contactsPK)) return;

			String nick = attempt._contact.nick().currentValue();
			
			Contact existing = findContactGivenPublicKey(remotePK);
			if (existing != null) {
				handleDuplicatePK(nick, existing);
				throw new IllegalParameter("Remote contact has same public key as another contact.");
			}
			
			if (!contactsPK.isEmpty()) notifyUserOfPKMismatch(nick);
			
			_businessSource.contactPublicKeyUpdater().consume(new ContactPublicKeyInfo(nick, remotePK));
		} };

	}

	private void handleDuplicatePK(String nick, Contact existing) {
		_user.acknowledgeNotification(nick + " has the same public key as " + existing.nick().currentValue() + ". You must delete one of them."); //Fix: Create an error state for the contact. 
	}

	private void notifyUserOfPKMismatch(String nick) {
		 //Fix: Security implementation: Revert the status of the contact to "unconfirmed" or something of the sort, so that the user has to confirm the remote PK again.
		String notification =
			" SECURITY ALERT FOR CONTACT: " + nick + "\n\n" +
			" Either this contact has changed its public key or\n" +
			" someone else is trying to trick you and impersonate it.\n\n" +
			" This contact's status will be changed to 'UNCONFIRMED',\n" +
			" so that you can confirm its public key again.";
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
	private Map<String, Channel> _channelsById = new HashMap<String, Channel>();
	private Map<ContactId, Mux> _muxesByContactId = new HashMap<ContactId, Mux>();

	
	private String prepareBusiness() {
		int sneerPort = _businessSource.output().sneerPort().currentValue();
		if (sneerPort == 0) initSneerPort(_businessSource);

		String id = _businessSource.output().publicKey().currentValue();
		System.out.println("id: " + id);
		if (id.isEmpty()) initId(_businessSource);
		return id;
	}


	public Channel getChannel(String channelId) {
		Channel result = _channelsById.get(channelId);
		if (result != null) return result;
		
		result = new ChannelImpl(channelId, myMuxProvider());
		_channelsById.put(channelId, result);
		return result;
	}

	private MuxProvider myMuxProvider() {
		return new MuxProvider() {
			public Mux muxFor(ContactId contactId) {
				Mux result = _muxesByContactId.get(contactId);
				if (result != null) return result;
				
				result = new Mux(connectionFor(contactId));
				_muxesByContactId.put(contactId, result);
				return result;
			}
		};
	}

	private Connection connectionFor(ContactId contactId) {
		return _spider.connectionFor(contactId);
	}

	private void initId(BusinessSource businessSource) {
		String id = "" + System.currentTimeMillis() + "/" + System.nanoTime();
		businessSource.publicKeySetter().consume(id);
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

	private void serve(final ObjectSocket socket) {
		String publicKey;
		String name;
		try {
			publicKey = (String)socket.readObject();
			name = (String)socket.readObject();
		} catch (Exception ignored) {
			return;
		}
		
		Contact contact = findContactGivenPublicKey(publicKey);
		
		try {
			if (contact == null) contact = produceContactWithNewPublicKey(name, publicKey);
		} catch (CancelledByUser e) {
			return;
		}

		try {
			socket.writeObject(ownPublicKey().currentValue());
		} catch (IOException ignored) {
			return;
		}
		
		_spider.connectionFor(contact.id()).serveIncomingSocket(socket);
	}


	private Contact produceContactWithNewPublicKey(String name, String publicKey) throws CancelledByUser {
		String prompt = " Someone claiming to be\n\n" + name + "\n\n is trying to connect to you. Do you want\n" +
		" to accept the connection?";
		if (!_user.confirm(prompt)) throw new CancelledByUser();

		String nick;
		Contact existing;
		while (true) {
			nick = _user.answer("Enter a nickname for your new contact:", name);
			
			existing = findContactGivenNick(nick);
			if (existing == null) return createContact(publicKey, nick);
			
			if (existing.publicKey().currentValue().isEmpty()) break;
			_user.acknowledgeNotification("There already is another contact with this nickname:\n\n" + nick, "Choose Another...");
		}
		
		_businessSource.contactPublicKeyUpdater().consume(new ContactPublicKeyInfo(nick, publicKey)); //Refactor: Use contactId instead of nick;
		
		return existing;
	}


	private Contact createContact(String publicKey, String nick) throws CancelledByUser {
		try {
			_businessSource.contactAdder().consume(new ContactInfo(nick, "", 0, publicKey)); //Implement: get actual host addresses from contact.
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
