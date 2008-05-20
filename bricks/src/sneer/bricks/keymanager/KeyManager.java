package sneer.bricks.keymanager;

import sneer.bricks.contacts.Contact;
import sneer.bricks.crypto.Sneer1024;
import sneer.bricks.mesh.Party;
import wheel.lang.Functor;

public interface KeyManager {

	Sneer1024 ownPublicKey();

	void addKey(Contact contact, Sneer1024 publicKey);
	Sneer1024 keyGiven(Contact contact);
	Contact contactGiven(Sneer1024 peersPublicKey);
	Contact contactGiven(Sneer1024 publicKey, Functor<Sneer1024, Contact> factoryToUseIfAbsent);

	Party partyGiven(Sneer1024 pk, Functor<Sneer1024, Party> factoryToUseIfAbsent);




}
