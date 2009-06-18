package sneer.bricks.pulp.keymanager.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.pulp.contacts.Contact;
import sneer.bricks.pulp.crypto.Crypto;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventNotifiers;
import sneer.bricks.pulp.events.EventSource;
import sneer.bricks.pulp.keymanager.KeyManager;
import sneer.foundation.brickness.PublicKey;
import sneer.foundation.commons.lang.Producer;

class KeyManagerImpl implements KeyManager {

	private PublicKey _ownKey;
	
	private final Map<Contact, PublicKey> _keyByContact = new HashMap<Contact, PublicKey>();

	private EventNotifier<Contact> _keyChanges = my(EventNotifiers.class).create();

	private final Crypto _crypto = my(Crypto.class);


	@Override
	public synchronized PublicKey ownPublicKey() {
		if (_ownKey == null)
			_ownKey = generateMickeyMouseKey();

		return _ownKey;
	}

	private PublicKey generateMickeyMouseKey() {
		String string = "" + System.currentTimeMillis() + System.nanoTime() + hashCode();
	
		return generateMickeyMouseKey(string);
	}

	@Override
	public PublicKey keyGiven(Contact contact) {
		return _keyByContact.get(contact);
	}


	@Override
	public synchronized void addKey(Contact contact, PublicKey publicKey) {
		if(keyGiven(contact) != null) throw new IllegalArgumentException("There already was a public key registered for contact: " + contact.nickname().currentValue());
		_keyByContact.put(contact, publicKey);
		_keyChanges.notifyReceivers(contact);
	}


	@Override
	public synchronized Contact contactGiven(PublicKey peersPublicKey) {
		for (Contact candidate : _keyByContact.keySet())
			if(_keyByContact.get(candidate).equals(peersPublicKey))
				return candidate;
		
		return null;
	}

	@Override
	public PublicKey unmarshall(byte[] bytes) {
		return new PublicKey(bytes);
	}

	@SuppressWarnings("deprecation")
	@Override
	public PublicKey generateMickeyMouseKey(String string) {
		Sneer1024 sneer1024 = _crypto.digest(string.getBytes());
		return new PublicKey(sneer1024.bytes());
	}

	@Override
	public EventSource<Contact> keyChanges() {
		return _keyChanges .output();
	}

	@Override
	public Contact contactGiven(PublicKey peersPublicKey, Producer<Contact> producerToUseIfAbsent) {
		Contact result = contactGiven(peersPublicKey);
		if (result != null) return result;
		
		result = producerToUseIfAbsent.produce();
		addKey(result, peersPublicKey);
		return result;	}
}
