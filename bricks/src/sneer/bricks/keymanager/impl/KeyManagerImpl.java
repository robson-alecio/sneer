package sneer.bricks.keymanager.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.contacts.Contact;
import sneer.bricks.crypto.Crypto;
import sneer.bricks.crypto.Sneer1024;
import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.keymanager.PublicKey;
import sneer.bricks.mesh.Party;
import sneer.lego.Inject;
import wheel.lang.Functor;

public class KeyManagerImpl implements KeyManager {

	private final PublicKey _ownKey = createMickeyMouseKey();
	
	private final Map<Contact, PublicKey> _keyByContact = new HashMap<Contact, PublicKey>();

	private final Map<PublicKey, Party> _partiesByPublicKey = new HashMap<PublicKey, Party>();

	@Inject
	private static Crypto _crypto;

	private PublicKey createMickeyMouseKey() {
		String string = "" + System.currentTimeMillis() + System.nanoTime() + hashCode();
		Sneer1024 sneer1024 = _crypto.digest(string.getBytes());
		return new PublicKeyImpl(sneer1024);
	}

	@Override
	public PublicKey ownPublicKey() {
		return _ownKey;
	}

	@Override
	public PublicKey keyGiven(Contact contact) {
		return _keyByContact.get(contact);
	}

	@Override
	public Party partyGiven(PublicKey pk) {
		return partyGiven(pk, null);
	}

	@Override
	public synchronized Party partyGiven(PublicKey pk, Functor<PublicKey, Party> factoryToUseIfAbsent) {
		Party result = _partiesByPublicKey.get(pk);
		if (result == null && factoryToUseIfAbsent != null) {
			result = factoryToUseIfAbsent.evaluate(pk);
			_partiesByPublicKey.put(pk, result);
		}
		return result;
	}

	@Override
	public synchronized Contact contactGiven(PublicKey peersPublicKey, Functor<PublicKey, Contact> factoryToUseIfAbsent) {
		Contact result = contactGiven(peersPublicKey);
		if (result != null) return result;
		
		result = factoryToUseIfAbsent.evaluate(peersPublicKey);
		addKey(result, peersPublicKey);
		return result;
	}


	@Override
	public synchronized void addKey(Contact contact, PublicKey publicKey) {
		if(keyGiven(contact) != null) throw new IllegalArgumentException("There already was a public key registered for contact: " + contact.nickname().currentValue());
		_keyByContact.put(contact, publicKey);
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
		return new PublicKeyImpl(_crypto.unmarshallSneer1024(bytes));
	}
}
