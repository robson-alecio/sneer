package sneer.bricks.mesh.impl;

import java.util.ArrayList;

public class Subscription implements Ambassador {

	private final ArrayList<String> _nicknamePath;
	private final String _signalPath;
	
	public Subscription(ArrayList<String> nicknamePath, String signalPath) {
		_nicknamePath = nicknamePath;
		_signalPath = signalPath;
	}

	public void visit(DirectProxy peer) {
		peer.serveSubscriptionTo(_nicknamePath, _signalPath);
	}


}
