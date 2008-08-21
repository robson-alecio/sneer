package sneer.pulp.mesh.impl;

import sneer.kernel.container.Brick;
import sneer.pulp.keymanager.PublicKey;

interface Visitable {

	void handleNotification(PublicKey publicKey, Class<? extends Brick> brickInterface, String signalPath, Object notification);

	void handleNotificationOfContactAdded(PublicKey publicKey,	RemoteContact newContact);

	void handleNotificationOfContactRemoved(PublicKey publicKey, RemoteContact contact);

	void serveSubscriptionTo(PublicKey publicKey, Class<? extends Brick> brickInterface, String signalName);

	void serveSubscriptionToContacts(PublicKey publicKey);


}
