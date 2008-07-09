package sneer.bricks.mesh.impl;

import sneer.bricks.keymanager.PublicKey;
import sneer.lego.Brick;

interface Visitable {

	void handleNotification(PublicKey publicKey, Class<? extends Brick> brickInterface, String signalPath, Object notification);

	void handleNotificationOfContactAdded(PublicKey publicKey,	RemoteContact newContact);

	void handleNotificationOfContactRemoved(PublicKey publicKey, RemoteContact contact);

	void serveSubscriptionTo(PublicKey publicKey, Class<? extends Brick> brickInterface, String signalName);

	void serveSubscriptionToContacts(PublicKey publicKey);


}
