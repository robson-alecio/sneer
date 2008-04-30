package sneer.bricks.mesh.impl;

import java.util.ArrayList;

public class RemoteProxy extends Proxy {

	private final String _givenNickname;
	private final Proxy _intermediary;

	RemoteProxy(Proxy intermediary, String givenNickname) {
		_intermediary = intermediary;
		_givenNickname = givenNickname;
	}


	@Override
	AbstractParty produceProxyFor(String nickname) {
		return new RemoteProxy(this, nickname);
	}


	@Override
	void subscribeTo(ArrayList<String> nicknamePath, String remoteSignalPath) {
		nicknamePath.add(0, _givenNickname);
		_intermediary.subscribeTo(nicknamePath, remoteSignalPath);
	}



}
