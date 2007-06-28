package sneer.kernel.communication.impl;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import sneer.kernel.business.Business;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.ContactInfo;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.impl.ChannelImpl.MuxProvider;
import wheel.io.Connection;
import wheel.io.Log;
import wheel.io.network.ObjectSocket;
import wheel.io.network.OldNetwork;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.lang.exceptions.IllegalParameter;

public class Communicator {



	public Communicator(User user, OldNetwork network, BusinessSource businessSource) {
		_user = user;
		_businessSource = businessSource;
		Business business = businessSource.output();
		
		prepareBusiness();
		
		new SocketAccepter(user, network, business.sneerPort(), mySocketServer());
		_spider = new Spider(business.publicKey().currentValue(), business.ownName(), network, business.contacts(), businessSource.contactOnlineSetter());
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
		
		if (contact == null) {
			String prompt = " Someone claiming to be\n\n" + name + "\n\n is trying to connect to you. Do you want\n" +
			" to accept the connection?";
			if (!_user.confirm(prompt)) return;
			
			String nick;
			try {
				nick = _user.answer("Enter a nickname for your new contact:", name);
			} catch (CancelledByUser e) {
				return;
			}
			
			try {
				_businessSource.contactAdder().consume(new ContactInfo(nick, "", 0, publicKey)); //Implement: get actual host addresses from contact.
			} catch (IllegalParameter e) {
				_user.acknowledge(e);
				return;
			}
			
			contact = findContactGivenNick(nick);
		}
		
		_spider.connectionFor(contact.id()).serveIncomingSocket(socket);
		
	}


	private Contact findContactGivenNick(String nick) {
		for (Contact contact : _businessSource.output().contacts())
			if (nick.equals(contact.nick().currentValue())) return contact;
		throw new IllegalStateException();
	}


	private Contact findContactGivenPublicKey(String publicKey) {
		for (Contact contact : _businessSource.output().contacts())
			if (publicKey.equals(contact.publicKey().currentValue())) return contact;
		return null;
	}

}
