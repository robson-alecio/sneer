package sneer.bricks.mesh.impl;

import sneer.bricks.crypto.Sneer1024;

class NotificationOfContactRemoved implements Ambassador {

	private final Sneer1024 _publicKey;
	private final RemoteContact _contact;

	public NotificationOfContactRemoved(Sneer1024 publicKey, RemoteContact contact) {
		_publicKey = publicKey;
		_contact = contact;
	}

	public void visit(SignalConnection connectionToPeer) {
		connectionToPeer.handleNotificationOfContactRemoved(_publicKey, _contact);
	}

}
