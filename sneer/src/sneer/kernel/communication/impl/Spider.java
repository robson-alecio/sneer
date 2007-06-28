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
	private final String _publicKey;
	private Map<ContactId, Connection> _connectionsByContactId = new HashMap<ContactId, Connection>();

	private class MyContactReceiver extends SimpleListReceiver {

		@Override
		public void elementAdded(int index) {
			Contact newContact = _contacts.currentGet(index);
			connectTo(newContact);
		}

		@Override
		public void elementRemoved(int index) {
			// Implement Auto-generated method stub
		}

	}

	
	private void connectTo(Contact contact) {
		ConnectionImpl newConnection = new ConnectionImpl(contact, _network, _publicKey, _onlineSetter);
		_connectionsByContactId.put(contact.id(), newConnection);
	}

	
	Connection connectionFor(ContactId contactId) {
		return _connectionsByContactId.get(contactId);
	}
	
}
