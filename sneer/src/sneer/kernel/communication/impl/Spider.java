package sneer.kernel.communication.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Operator;
import wheel.io.network.OldNetwork;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.SimpleListReceiver;

class Spider implements Operator {

	Spider(OldNetwork network, ListSignal<ContactAttributes> contacts, Consumer<OutgoingConnectionAttempt> outgoingConnectionValidator, Omnivore<Object> objectReceiver) {
		_network = network;
		_contacts = contacts;
		_outgoingConnectionValidator = outgoingConnectionValidator;
		_objectReceiver = objectReceiver;
		
		new MyContactReceiver(_contacts);
	}
	
	private final OldNetwork _network;
	private final ListSignal<ContactAttributes> _contacts;
	private final Consumer<OutgoingConnectionAttempt> _outgoingConnectionValidator;
	private final Omnivore<Object> _objectReceiver;

	private final Map<ContactId, ConnectionImpl> _connectionsByContactId = new HashMap<ContactId, ConnectionImpl>();

	private class MyContactReceiver extends SimpleListReceiver<ContactAttributes> {

		public MyContactReceiver(ListSignal<ContactAttributes> contacts) {
			super(contacts);
		}

		@Override
		public void elementPresent(ContactAttributes contact) {
			connectTo(contact);
		}
		
		@Override
		public void elementAdded(ContactAttributes newContact) {
			connectTo(newContact);
		}

		@Override
		public void elementToBeRemoved(ContactAttributes contact) {
			disconnect(contact);
		}

	}

	private void connectTo(ContactAttributes contact) {
		ConnectionImpl newConnection = new ConnectionImpl(contact, _network, _outgoingConnectionValidator, _objectReceiver);
		_connectionsByContactId.put(contact.id(), newConnection);
	}

	
	private void disconnect(ContactAttributes contact) {
		connectMeWith(contact.id()).close();
		_connectionsByContactId.remove(contact.id());
	}

	@Override
	public ConnectionImpl connectMeWith(ContactId contactId) {
		return _connectionsByContactId.get(contactId);
	}
	
}
