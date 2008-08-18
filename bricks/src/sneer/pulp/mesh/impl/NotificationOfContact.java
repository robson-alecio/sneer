package sneer.pulp.mesh.impl;

import sneer.pulp.keymanager.PublicKey;

class NotificationOfContact implements Ambassador {

	private final PublicKey _publicKey;
	private final RemoteContact _newContact;

	public NotificationOfContact(PublicKey publicKey, RemoteContact newContact) {
		_publicKey = publicKey;
		_newContact = newContact;
	}

	public void visit(Visitable visitable) {
		visitable.handleNotificationOfContactAdded(_publicKey, _newContact);
	}

}
