package sneer.pulp.keymanager;

import sneer.brickness.Brick;
import sneer.brickness.PublicKey;
import sneer.commons.lang.Producer;
import sneer.pulp.contacts.Contact;
import sneer.pulp.events.EventSource;

@Brick
public interface KeyManager {

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
