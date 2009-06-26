package sneer.bricks.pulp.keymanager;

import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.events.EventSource;
import sneer.foundation.brickness.Brick;
import sneer.foundation.brickness.PublicKey;

@Brick
public interface KeyManager {

	PublicKey ownPublicKey();

//	void addKey(Contact contact, PublicKey publicKey);
	PublicKey keyGiven(Contact contact);
	Contact contactGiven(PublicKey peersPublicKey);
//	Contact contactGiven(PublicKey publicKey, Producer<Contact> producerToUseIfAbsent);

	EventSource<Contact> keyChanges();
	
	PublicKey unmarshall(byte[] publicKeyBytes);

}
