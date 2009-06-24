package sneer.bricks.pulp.keymanager;

import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.events.EventSource;
import sneer.foundation.brickness.Brick;
import sneer.foundation.brickness.PublicKey;
import sneer.foundation.lang.Producer;

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
