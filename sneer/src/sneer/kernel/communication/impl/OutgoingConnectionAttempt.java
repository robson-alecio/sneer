package sneer.kernel.communication.impl;

import sneer.kernel.business.contacts.Contact;
import wheel.io.network.ObjectSocket;


class OutgoingConnectionAttempt {

	final Contact _contact;
	final ObjectSocket _outgoingSocket;

	public OutgoingConnectionAttempt(Contact contact, ObjectSocket outgoingSocket) {
		_contact = contact;
		_outgoingSocket = outgoingSocket;
	}

}
