package sneer.bricks.mesh.impl;

public class SignalSubscription implements Packet, java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String _signalPath;
	
	public SignalSubscription(String signalPath) {
		_signalPath = signalPath;
	}


}
