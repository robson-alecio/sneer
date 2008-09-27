package sneer.pulp.mesh.impl;

import sneer.pulp.keymanager.PublicKey;

class SubscriptionToContacts implements Ambassador {

	private final PublicKey _publicKey;
	
	SubscriptionToContacts(PublicKey publicKey) {
		_publicKey = publicKey;
	}

	public void visit(Visitable visitable) {
		visitable.serveSubscriptionToContacts(_publicKey);
	}
}
