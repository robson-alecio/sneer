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
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.SimpleListReceiver;

class Spider {



	Spider(String publicKey, Signal<String> ownName, OldNetwork network,  ListSignal<Contact> contacts, Omnivore<OnlineEvent> onlineSetter, Omnivore<Connection> newConnectionHandler) {
		_publicKey = publicKey;
		_ownName = ownName;
		
		_network = network;
		_contacts = contacts;
		_onlineSetter = onlineSetter;
		_newConnectionHandler = newConnectionHandler;
		
		_contacts.addListReceiver(new MyContactReceiver());
	}
	
	private final OldNetwork _network;
	private final ListSignal<Contact> _contacts;
	private final Omnivore<OnlineEvent> _onlineSetter;
	private final String _publicKey;
	private final Signal<String> _ownName;
	private final Omnivore<Connection> _newConnectionHandler;

	private Map<ContactId, ConnectionImpl> _connectionsByContactId = new HashMap<ContactId, ConnectionImpl>();

	private class MyContactReceiver extends SimpleListReceiver {

		@Override
		public void elementAdded(int index) {
			Contact newContact = _contacts.currentGet(index);
			connectTo(newContact);
		}

		@Override
		public void elementRemoved(int index) {
			throw new NotImplementedYet(); // Implement
		}

	}

	
	private void connectTo(Contact contact) {
		ConnectionImpl newConnection = new ConnectionImpl(contact, _network, _publicKey, _ownName, _onlineSetter, _newConnectionHandler);
		_connectionsByContactId.put(contact.id(), newConnection);
	}

	
	ConnectionImpl connectionFor(ContactId contactId) {
		return _connectionsByContactId.get(contactId);
	}
	
}
