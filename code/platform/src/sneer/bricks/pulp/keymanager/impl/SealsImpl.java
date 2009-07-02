package sneer.bricks.pulp.keymanager.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.UnsupportedEncodingException;

import sneer.bricks.network.social.Contact;
import sneer.bricks.network.social.ContactManager;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.own.name.OwnNameKeeper;
import sneer.foundation.brickness.Seal;

class SealsImpl implements Seals {

//	private PublicKey _ownKey;
//	
//	private final Map<Contact, PublicKey> _keyByContact = new HashMap<Contact, PublicKey>();
//
//	private final Crypto _crypto = my(Crypto.class);

	private static final String UTF_8 = "UTF-8";


	@Override
	public synchronized Seal ownSeal() {
		return generateMickeyMouseKey(ownName());
	}

	@Override
	public Seal keyGiven(Contact contact) {
		//return _keyByContact.get(contact);
		return generateMickeyMouseKey(contact.nickname().currentValue());
	}


//	@Override
//	public synchronized void addKey(Contact contact, PublicKey publicKey) {
//		if(keyGiven(contact) != null) throw new IllegalArgumentException("There already was a public key registered for contact: " + contact.nickname().currentValue());
//		_keyByContact.put(contact, publicKey);
//	}


	@Override
	public synchronized Contact contactGiven(Seal peersSeal) {
//		for (Contact candidate : _keyByContact.keySet())
//			if(_keyByContact.get(candidate).equals(peersPublicKey))
//				return candidate;
//		
//		return null;
		
		return my(ContactManager.class).produceContact(nameFor(peersSeal));
	}

	private String nameFor(Seal seal) {
		try {
			return new String(seal.bytes(), UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public Seal unmarshall(byte[] bytes) {
		return new Seal(bytes);
	}

	private Seal generateMickeyMouseKey(String name) {
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
