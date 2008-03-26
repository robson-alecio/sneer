package sneer.bricks.connection;

import sneer.contacts.ContactId;

public interface KeyManager {

	byte[] ownPublicKey();

	ContactId contactIdGiven(byte[] peersPublicKey);

}
