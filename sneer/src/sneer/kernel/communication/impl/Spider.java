package sneer.kernel.communication.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.OnlineEvent;
import wheel.io.Connection;
import wheel.io.network.OldNetwork;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.SimpleListReceiver;

class Spider {

	Spider(OldNetwork network, ListSignal<Contact> contacts, Omnivore<OnlineEvent> onlineSetter,  Consumer<OutgoingConnectionAttempt> outgoingConnectionValidator) {
		_network = network;
		_contacts = contacts;
		_onlineSetter = onlineSetter;
		_outgoingConnectionValidator = outgoingConnectionValidator;
		
		new MyContactReceiver(_contacts);
	}
	
	private final OldNetwork _network;
	private final ListSignal<Contact> _contacts;
	private final Omnivore<OnlineEvent> _onlineSetter;
	private final Consumer<OutgoingConnectionAttempt> _outgoingConnectionValidator;

	private final Map<ContactId, ConnectionImpl> _connectionsByContactId = new HashMap<ContactId, ConnectionImpl>();

	private class MyContactReceiver extends SimpleListReceiver<Contact> {

		public MyContactReceiver(ListSignal<Contact> contacts) {
			super(contacts);
		}

		@Override
		public void elementPresent(Contact contact) {
			connectTo(contact);
		}
		
		@Override
		public void elementAdded(Contact newContact) {
			connectTo(newContact);
		}

		@Override
		public void elementToBeRemoved(Contact contact) {
			disconnect(contact);
		}

	}

	private void connectTo(Contact contact) {
		ConnectionImpl newConnection = new ConnectionImpl(contact, _network, _onlineSetter, _outgoingConnectionValidator);
		_connectionsByContactId.put(contact.id(), newConnection);
	}

	
	private void disconnect(Contact contact) {
		connectionFor(contact.id()).close();
		_connectionsByContactId.remove(contact.id());
	}


	ConnectionImpl connectionFor(ContactId contactId) {
		return _connectionsByContactId.get(contactId);
	}
	
}
