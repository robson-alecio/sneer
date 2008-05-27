package sneer.bricks.mesh.impl;

import sneer.bricks.keymanager.PublicKey;

class Notification implements Ambassador {

	private final PublicKey _publicKey;
	private final String _signalPath;
	private final Object _newValue;

	public Notification(PublicKey publicKey, String signalPath, Object newValue) {
		_publicKey = publicKey;
		_signalPath = signalPath;
		_newValue = newValue;
	}

	public void visit(SignalConnection connectionToPeer) {
		connectionToPeer.handleNotification(_publicKey, _signalPath, _newValue);
	}

}
