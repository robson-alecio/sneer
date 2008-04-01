package sneer.bricks.mesh.impl;

public class SignalSubscription implements Packet {

	private final String _nicknamePath;
	
	private final String _signalPath;
	
	public SignalSubscription(String nicknamePath, String signalPath) {
		_nicknamePath = nicknamePath;
		_signalPath = signalPath;
	}


}
