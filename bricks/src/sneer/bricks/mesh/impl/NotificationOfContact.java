package sneer.bricks.mesh.impl;

import sneer.bricks.crypto.Sneer1024;

class NotificationOfContact implements Ambassador {

	private final Sneer1024 _publicKey;
	private final RemoteContact _newContact;

	public NotificationOfContact(Sneer1024 publicKey, RemoteContact newContact) {
		_publicKey = publicKey;
		_newContact = newContact;
	}

	public void visit(SignalConnection connectionToPeer) {
		connectionToPeer.handleNotificationOfContactAdded(_publicKey, _newContact);
	}

}
