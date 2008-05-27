package sneer.bricks.mesh.impl;

import sneer.bricks.keymanager.PublicKey;

public class Subscription implements Ambassador {

	private final PublicKey _publicKey;
	private final String _signalPath;
	
	public Subscription(PublicKey publicKey, String signalPath) {
		_publicKey = publicKey;
		_signalPath = signalPath;
	}

	public void visit(SignalConnection connectionToPeer) {
		connectionToPeer.serveSubscriptionTo(_publicKey, _signalPath);
	}


}
