package sneer.pulp.keymanager;

import sneer.kernel.container.Brick;
import sneer.kernel.container.PublicKey;
import sneer.pulp.contacts.Contact;
import wheel.lang.Producer;
import wheel.reactive.EventSource;

public interface KeyManager extends Brick {

	PublicKey ownPublicKey();

	void addKey(Contact contact, PublicKey publicKey);
	PublicKey keyGiven(Contact contact);
	Contact contactGiven(PublicKey peersPublicKey);
	Contact contactGiven(PublicKey publicKey, Producer<Contact> producerToUseIfAbsent);

	EventSource<Contact> keyChanges();
	
//	Party partyGiven(PublicKey pk, Functor<PublicKey, Party> factoryToUseIfAbsent);
//	Party partyGiven(PublicKey pk);

	PublicKey unmarshall(byte[] publicKeyBytes);

	@Deprecated
	PublicKey generateMickeyMouseKey(String nick); //Just until we implement real keys.
}
