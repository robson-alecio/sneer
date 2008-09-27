package sneer.pulp.mesh.impl;

import sneer.pulp.contacts.Contact;
import sneer.pulp.keymanager.PublicKey;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

public class RemoteContact implements Contact {

	private final PublicKey _publicKey;
	private final RegisterImpl<String> _nickname;
	
	RemoteContact(PublicKey publicKey, String nickname) {
		_publicKey = publicKey;
		_nickname = new RegisterImpl<String>(nickname);
	}
	
	@Override
	public Signal<String> nickname() {
		return _nickname.output();
	}

	PublicKey publicKey() {
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
