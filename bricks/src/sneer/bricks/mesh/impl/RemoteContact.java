package sneer.bricks.mesh.impl;

import sneer.bricks.contacts.Contact;
import sneer.bricks.crypto.Sneer1024;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

public class RemoteContact implements Contact {


	RemoteContact(Sneer1024 publicKey, String nickname) {
		_publicKey = publicKey;
		_nickname = new RegisterImpl<String>(nickname);
	}
	
	
	private final Sneer1024 _publicKey;
	private final RegisterImpl<String> _nickname;

	
	@Override
	public Signal<String> nickname() {
		return _nickname.output();
	}

	Sneer1024 publicKey() {
		return _publicKey;
	}

	@Override
	public int hashCode() {
		return _publicKey.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final RemoteContact other = (RemoteContact) obj;
		if (_publicKey == null) {
			if (other._publicKey != null)
				return false;
		} else if (!_publicKey.equals(other._publicKey))
			return false;
		return true;
	}

	Omnivore<String> nicknameSetter() {
		return _nickname.setter();
	}

}
