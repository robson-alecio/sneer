package sneer.bricks.mesh.impl;

import sneer.bricks.crypto.Sneer1024;

public class Subscription implements Ambassador {

	private final Sneer1024 _publicKey;
	private final String _signalPath;
	
	public Subscription(Sneer1024 publicKey, String signalPath) {
		_publicKey = publicKey;
		_signalPath = signalPath;
	}

	public void visit(SignalConnection connectionToPeer) {
		connectionToPeer.serveSubscriptionTo(_publicKey, _signalPath);
	}


}
