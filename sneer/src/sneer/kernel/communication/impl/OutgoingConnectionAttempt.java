package sneer.kernel.communication.impl;

import sneer.kernel.business.contacts.ContactAttributes;
import wheel.io.network.ObjectSocket;


class OutgoingConnectionAttempt {

	final ContactAttributes _contact;
	final ObjectSocket _outgoingSocket;

	public OutgoingConnectionAttempt(ContactAttributes contact, ObjectSocket outgoingSocket) {
		_contact = contact;
		_outgoingSocket = outgoingSocket;
	}

}
