package sneer.bricks.keymanager;

import sneer.bricks.contacts.Contact;
import sneer.bricks.crypto.Sneer1024;
import sneer.bricks.mesh.Party;
import wheel.lang.Functor;

public interface KeyManager {

	Sneer1024 ownPublicKey();

	void addKey(Contact contact, Sneer1024 peersPublicKey) throws ContactAlreadyHadAKey, KeyBelongsToOtherContact;
	Contact contactGiven(Sneer1024 publicKey);
	Sneer1024 keyGiven(Contact contact);

	Party partyGiven(Sneer1024 pk, Functor<Sneer1024, Party> factoryToUseIfAbsent);

}
