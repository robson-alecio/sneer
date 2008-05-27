package sneer.bricks.keymanager;

import sneer.bricks.contacts.Contact;
import sneer.bricks.mesh.Party;
import wheel.lang.Functor;

public interface KeyManager {

	PublicKey ownPublicKey();

	void addKey(Contact contact, PublicKey publicKey);
	PublicKey keyGiven(Contact contact);
	Contact contactGiven(PublicKey peersPublicKey);
	Contact contactGiven(PublicKey publicKey, Functor<PublicKey, Contact> factoryToUseIfAbsent);

	Party partyGiven(PublicKey pk, Functor<PublicKey, Party> factoryToUseIfAbsent);
	Party partyGiven(PublicKey pk);

	PublicKey unmarshall(byte[] publicKeyBytes);
}
