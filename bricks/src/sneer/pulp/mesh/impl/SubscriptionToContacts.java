package sneer.pulp.mesh.impl;

import sneer.pulp.keymanager.PublicKey;

public class SubscriptionToContacts implements Ambassador {

	private final PublicKey _publicKey;
	
	public SubscriptionToContacts(PublicKey publicKey) {
		_publicKey = publicKey;
	}

	public void visit(Visitable visitable) {
		visitable.serveSubscriptionToContacts(_publicKey);
	}


}
