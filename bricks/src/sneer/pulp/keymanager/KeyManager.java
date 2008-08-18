package sneer.pulp.keymanager;

import sneer.lego.Brick;
import sneer.pulp.contacts.Contact;
import sneer.pulp.mesh.Party;
import wheel.lang.Functor;

public interface KeyManager extends Brick {

	PublicKey ownPublicKey();

	void addKey(Contact contact, PublicKey publicKey);
	PublicKey keyGiven(Contact contact);
	Contact contactGiven(PublicKey peersPublicKey);
	Contact contactGiven(PublicKey publicKey, Functor<PublicKey, Contact> factoryToUseIfAbsent);

	Party partyGiven(PublicKey pk, Functor<PublicKey, Party> factoryToUseIfAbsent);
	Party partyGiven(PublicKey pk);

	PublicKey unmarshall(byte[] publicKeyBytes);
}
