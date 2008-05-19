package sneer.bricks.mesh.impl;

import sneer.bricks.contacts.Contact;
import sneer.bricks.crypto.Sneer1024;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

public class RemoteContact implements Contact {

	RemoteContact(Sneer1024 publicKey, String nickname) {
		_publicKey = publicKey;
		_nickname = nickname;
	}
	
	
	private final Sneer1024 _publicKey;
	private final String _nickname;

	
	@Override
	public Signal<String> nickname() {
		return new RegisterImpl<String>(_nickname).output();
	}

	Sneer1024 publicKey() {
		return _publicKey;
	}

}
