package sneer.bricks.pulp.keymanager.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.UnsupportedEncodingException;

import sneer.bricks.network.social.Contact;
import sneer.bricks.network.social.ContactManager;
import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventNotifiers;
import sneer.bricks.pulp.events.EventSource;
import sneer.bricks.pulp.keymanager.KeyManager;
import sneer.bricks.pulp.own.name.OwnNameKeeper;
import sneer.foundation.brickness.PublicKey;

class KeyManagerImpl implements KeyManager {

//	private PublicKey _ownKey;
//	
//	private final Map<Contact, PublicKey> _keyByContact = new HashMap<Contact, PublicKey>();
//
//	private final Crypto _crypto = my(Crypto.class);

	private static final String UTF_8 = "UTF-8";
	private EventNotifier<Contact> _keyChanges = my(EventNotifiers.class).create();


	@Override
	public synchronized PublicKey ownPublicKey() {
		return generateMickeyMouseKey(ownName());
	}

	@Override
	public PublicKey keyGiven(Contact contact) {
		//return _keyByContact.get(contact);
		return generateMickeyMouseKey(contact.nickname().currentValue());
	}


//	@Override
//	public synchronized void addKey(Contact contact, PublicKey publicKey) {
//		if(keyGiven(contact) != null) throw new IllegalArgumentException("There already was a public key registered for contact: " + contact.nickname().currentValue());
//		_keyByContact.put(contact, publicKey);
//		_keyChanges.notifyReceivers(contact);
//	}


	@Override
	public synchronized Contact contactGiven(PublicKey peersPublicKey) {
//		for (Contact candidate : _keyByContact.keySet())
//			if(_keyByContact.get(candidate).equals(peersPublicKey))
//				return candidate;
//		
//		return null;
		
		return my(ContactManager.class).produceContact(nameFor(peersPublicKey));
	}

	private String nameFor(PublicKey publicKey) {
		try {
			return new String(publicKey.bytes(), UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public PublicKey unmarshall(byte[] bytes) {
		return new PublicKey(bytes);
	}

	private PublicKey generateMickeyMouseKey(String name) {
//		Sneer1024 sneer1024 = _crypto.digest(string.getBytes());
//		return new PublicKey(sneer1024.bytes());
		
		byte[] bytes;
		try {
			bytes = name.getBytes(UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		} 
		return unmarshall(bytes);
	}

	@Override
	public EventSource<Contact> keyChanges() {
		return _keyChanges.output();
	}

//	@Override
//	public Contact contactGiven(PublicKey peersPublicKey, Producer<Contact> producerToUseIfAbsent) {
//		Contact result = contactGiven(peersPublicKey);
//		if (result != null) return result;
//		
//		result = producerToUseIfAbsent.produce();
//		addKey(result, peersPublicKey);
//		return result;
//	}
	
	private String ownName() {
		return my(OwnNameKeeper.class).name().currentValue();
	}

}
