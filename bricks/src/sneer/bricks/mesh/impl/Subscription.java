package sneer.bricks.mesh.impl;

public class Subscription implements Ambassador {

	private final String _signalPath;
	
	public Subscription(String signalPath) {
		_signalPath = signalPath;
	}

	public void visit(PeerImpl peer) {
		peer.serveSubscriptionTo(_signalPath);
	}


}
