package sneer.bricks.mesh.impl;

import sneer.bricks.crypto.Sneer1024;

public class SubscriptionToContacts implements Ambassador {

	private final Sneer1024 _publicKey;
	
	public SubscriptionToContacts(Sneer1024 publicKey) {
		_publicKey = publicKey;
	}

	public void visit(SignalConnection connectionToPeer) {
		connectionToPeer.serveSubscriptionToContacts(_publicKey);
	}


}
