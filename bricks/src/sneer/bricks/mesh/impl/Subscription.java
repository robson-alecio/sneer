package sneer.bricks.mesh.impl;

import sneer.bricks.keymanager.PublicKey;
import sneer.lego.Brick;

public class Subscription implements Ambassador {

	private final PublicKey _publicKey;
	private final String _signalName;
	private final Class<? extends Brick> _brickInterface;
	
	Subscription(PublicKey targetPK, Class<? extends Brick> brickInterface, String signalName) {
		_publicKey = targetPK;
		_signalName = signalName;
		_brickInterface = brickInterface;
	}

	public void visit(Visitable visitable) {
		visitable.serveSubscriptionTo(_publicKey, _brickInterface, _signalName);
	}


}
