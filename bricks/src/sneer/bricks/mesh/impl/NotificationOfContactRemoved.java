package sneer.bricks.mesh.impl;

import sneer.bricks.keymanager.PublicKey;

class NotificationOfContactRemoved implements Ambassador {

	private final PublicKey _publicKey;
	private final RemoteContact _contact;

	public NotificationOfContactRemoved(PublicKey publicKey, RemoteContact contact) {
		_publicKey = publicKey;
		_contact = contact;
	}

	public void visit(SignalConnection connectionToPeer) {
		connectionToPeer.handleNotificationOfContactRemoved(_publicKey, _contact);
	}

}
