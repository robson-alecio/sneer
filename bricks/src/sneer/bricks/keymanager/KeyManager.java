package sneer.bricks.keymanager;

import sneer.bricks.contacts.Contact;

public interface KeyManager {

	byte[] ownPublicKey();

	Contact contactGiven(byte[] peersPublicKey);

	void addKey(Contact contact, byte[] peersPublicKey)
		throws ContactAlreadyHadAKey, KeyBelongsToOtherContact;

}
