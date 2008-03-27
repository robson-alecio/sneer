package sneer.bricks.connection;

import sneer.contacts.Contact;

public interface KeyManager {

	byte[] ownPublicKey();

	Contact contactGiven(byte[] peersPublicKey);

	void addKey(Contact contact, byte[] peersPublicKey);

}
