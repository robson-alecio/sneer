package sneer.bricks.mesh.impl;

public class SignalSubscription implements Ambassador, java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String _signalPath;
	
	public SignalSubscription(String signalPath) {
		_signalPath = signalPath;
	}

	public void visit(PeerImpl peerImpl) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}


}
