package sneer.pulp.mesh.impl;

import sneer.lego.Brick;
import sneer.pulp.keymanager.PublicKey;

class Notification implements Ambassador {

	private final PublicKey _publicKey;
	private final Class<? extends Brick> _brickInterface;
	private final String _signalName;
	private final Object _payload;

	public Notification(PublicKey publicKey, Class<? extends Brick> brickInterface, String signalName, Object notification) {
		_publicKey = publicKey;
		_brickInterface = brickInterface;
		_signalName = signalName;
		_payload = notification;
	}

	public void visit(Visitable visitable) {
		visitable.handleNotification(_publicKey, _brickInterface, _signalName, _payload);
	}

}
