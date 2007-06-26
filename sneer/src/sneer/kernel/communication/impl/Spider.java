package sneer.kernel.communication.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.OnlineEvent;
import wheel.io.Connection;
import wheel.io.network.ObjectSocket;
import wheel.io.network.OldNetwork;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.SimpleListReceiver;

class Spider {

	Spider(String publicKey, OldNetwork network,  ListSignal<Contact> contacts, Omnivore<OnlineEvent> onlineSetter) {
		_publicKey = publicKey;
		
		_network = network;
		_contacts = contacts;
		_onlineSetter = onlineSetter;
		
		_contacts.addListReceiver(new MyContactReceiver());
	}
	
	private final OldNetwork _network;
	private final ListSignal<Contact> _contacts;
	private final Omnivore<OnlineEvent> _onlineSetter;
	private final Map<String, ObjectSocket> _socketsByAddress = new HashMap<String, ObjectSocket>();
	private final String _publicKey;
	private Map<ContactId, Connection> _connectionsByContactId = new HashMap<ContactId, Connection>();

	private class MyContactReceiver extends SimpleListReceiver {

		@Override
		public void elementAdded(int index) {
			Contact contact = _contacts.currentGet(index);
			_connectionsByContactId.put(contact.id(), new ConnectionImpl(contact));
			
			startIsOnlineWatchdog(contact);
		}

		@Override
		public void elementRemoved(int index) {
			// Implement Auto-generated method stub
		}

	}

	private void startIsOnlineWatchdog(final Contact contact) {
		Threads.startDaemon(new Runnable(){
			@Override
			public void run() {
				while (true) {
					barkTo(contact);
					Threads.sleepWithoutInterruptions(5000);
				}
			}
		});
	}

	private void barkTo(Contact contact) {
		boolean isOnline = isOnline(contact);

		Boolean wasOnline = contact.isOnline().currentValue();
		if (isOnline == wasOnline) return;
		
		String nick = contact.nick().currentValue();
		_onlineSetter.consume(new OnlineEvent(nick, isOnline));
	}

	private boolean isOnline(Contact contact) {
		String host = contact.host().currentValue();
		int port = contact.port().currentValue();
		
		try {
			produceSocket(host, port).writeObject("Bark");
			return true;
		} catch (IOException e) {
			discardSocket(host, port);
			return false;
		}
	}

	private void discardSocket(String host, int port) {
		_socketsByAddress.remove(address(host, port));
	}

	private ObjectSocket produceSocket(String host, int port) throws IOException {
		String address = address(host, port);
		ObjectSocket result = _socketsByAddress.get(address);
		
		if (result == null) {
			result = openSocket(host, port);
			_socketsByAddress.put(address, result);
		}
		
		return result;
	}

	private ObjectSocket openSocket(String host, int port) throws IOException {
		ObjectSocket result = _network.openSocket(host, port);
		result.writeObject(_publicKey);
		return result;
	}

	private String address(String host, int port) {
		return host + ":" + port;
	}

	Connection connectionFor(ContactId contactId) {
		return _connectionsByContactId.get(contactId);
	}
	
}
