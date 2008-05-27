package sneer.bricks.mesh.impl;

import sneer.bricks.keymanager.PublicKey;

class NotificationOfContact implements Ambassador {

	private final PublicKey _publicKey;
	private final RemoteContact _newContact;

	public NotificationOfContact(PublicKey publicKey, RemoteContact newContact) {
		_publicKey = publicKey;
		_newContact = newContact;
	}

	public void visit(SignalConnection connectionToPeer) {
		connectionToPeer.handleNotificationOfContactAdded(_publicKey, _newContact);
	}

}
