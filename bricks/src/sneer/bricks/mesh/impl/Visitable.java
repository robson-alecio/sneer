package sneer.bricks.mesh.impl;

import sneer.bricks.keymanager.PublicKey;

interface Visitable {

	void handleNotification(PublicKey publicKey, String signalPath, Object newValue);

	void handleNotificationOfContactAdded(PublicKey publicKey,	RemoteContact newContact);

	void handleNotificationOfContactRemoved(PublicKey publicKey, RemoteContact contact);

	void serveSubscriptionTo(PublicKey publicKey, String signalPath);

	void serveSubscriptionToContacts(PublicKey publicKey);

}
