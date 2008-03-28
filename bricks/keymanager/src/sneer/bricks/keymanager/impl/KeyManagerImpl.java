package sneer.bricks.keymanager.impl;

import sneer.bricks.keymanager.KeyManager;
import sneer.contacts.Contact;

public class KeyManagerImpl implements KeyManager {

	@Override
	public void addKey(Contact contact, byte[] peersPublicKey) {
	}

	@Override
	public Contact contactGiven(byte[] peersPublicKey) {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public byte[] ownPublicKey() {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

}
