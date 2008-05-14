package sneer.bricks.mesh.impl;

import sneer.bricks.crypto.Sneer1024;

class Notification implements Ambassador {

	private final Sneer1024 _publicKey;
	private final String _signalPath;
	private final Object _newValue;

	public Notification(Sneer1024 publicKey, String signalPath, Object newValue) {
		_publicKey = publicKey;
		_signalPath = signalPath;
		_newValue = newValue;
	}

	public void visit(SignalConnection connectionToPeer) {
		connectionToPeer.handleNotification(_publicKey, _signalPath, _newValue);
	}

}
