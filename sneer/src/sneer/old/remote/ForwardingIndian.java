package sneer.old.remote;

import sneer.old.life.LifeView;
import wheelexperiments.environment.network.ObjectSocket;

public class ForwardingIndian implements Indian {
	
	private static final long serialVersionUID = 1L;
	
	private Indian _delegate;
	private String _nickname;

	public ForwardingIndian(String nickname, Indian delegate) {
		_delegate = delegate;
		_nickname = nickname;
	}

	public void reportAbout(LifeView life, ObjectSocket socket) {
		_delegate.reportAbout(life.contact(_nickname), socket);
	}

	public void receive(SmokeSignal smokeSignal) {
		_delegate.receive(smokeSignal);
	}

	public int id() {
		return _delegate.id();
	}

	public String executeOn(LifeView lifeView) {
		throw new IllegalStateException();
	}
}
