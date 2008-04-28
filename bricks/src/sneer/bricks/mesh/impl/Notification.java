package sneer.bricks.mesh.impl;

class Notification implements Ambassador {

	private final String _signalPath;
	private final Object _newValue;

	public Notification(String signalPath, Object newValue) {
		_signalPath = signalPath;
		_newValue = newValue;
	}

	public void visit(DirectProxy peer) {
		peer.handleNotification(_signalPath, _newValue);
	}

}
