package sneer.bricks.mesh.impl;

import java.util.ArrayList;

class Notification implements Ambassador {

	private final ArrayList<String> _nicknamePath;
	private final String _signalPath;
	private final Object _newValue;

	public Notification(ArrayList<String> nicknamePath, String signalPath, Object newValue) {
		_nicknamePath = nicknamePath;
		_signalPath = signalPath;
		_newValue = newValue;
	}

	public void visit(DirectProxy peer) {
		peer.handleNotification(_nicknamePath, _signalPath, _newValue);
	}

}
