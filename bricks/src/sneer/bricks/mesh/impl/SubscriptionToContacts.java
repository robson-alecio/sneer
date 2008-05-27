package sneer.bricks.mesh.impl;

import sneer.bricks.keymanager.PublicKey;

public class SubscriptionToContacts implements Ambassador {

	private final PublicKey _publicKey;
	
	public SubscriptionToContacts(PublicKey publicKey) {
		_publicKey = publicKey;
	}

	public void visit(SignalConnection connectionToPeer) {
		connectionToPeer.serveSubscriptionToContacts(_publicKey);
	}


}
