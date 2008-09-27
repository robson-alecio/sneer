package sneer.pulp.mesh.impl;

import sneer.pulp.keymanager.PublicKey;

class NotificationOfContactRemoved implements Ambassador {

	private final PublicKey _publicKey;
	private final RemoteContact _contact;

	NotificationOfContactRemoved(PublicKey publicKey, RemoteContact contact) {
		_publicKey = publicKey;
		_contact = contact;
	}

	public void visit(Visitable visitable) {
		visitable.handleNotificationOfContactRemoved(_publicKey, _contact);
	}

}
