package sneer.bricks.mesh.impl;

import java.util.ArrayList;

import wheel.reactive.Signal;

public class RemoteProxy extends AbstractParty {

	private final String _givenNickname;
	private final AbstractParty _intermediary;

	RemoteProxy(AbstractParty intermediary, String givenNickname) {
		_intermediary = intermediary;
		_givenNickname = givenNickname;
	}

	@Override
	public <S> Signal<S> signal(String signalPath) {
		return _intermediary.signal(signalPath, new ArrayList<String>());
	}

	@Override
	<S> Signal<S> signal(String signalPath, ArrayList<String> nicknamePath) {
		nicknamePath.add(0, _givenNickname);
		return _intermediary.signal(signalPath, nicknamePath);
	}

	@Override
	void crash() {}

	@Override
	AbstractParty produceProxyFor(String nickname) {
		return new RemoteProxy(this, nickname);
	}

}
