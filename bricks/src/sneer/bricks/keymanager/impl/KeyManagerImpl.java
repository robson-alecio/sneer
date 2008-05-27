package sneer.bricks.keymanager.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.contacts.Contact;
import sneer.bricks.crypto.Crypto;
import sneer.bricks.crypto.Sneer1024;
import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.mesh.Party;
import sneer.lego.Inject;
import wheel.lang.Functor;

public class KeyManagerImpl implements KeyManager {

	@Inject
	static private Crypto _crypto; 
	
	private final Sneer1024 _ownKey = createMickeyMouseKey();
	
	private final Map<Contact, Sneer1024> _keyByContact = new HashMap<Contact, Sneer1024>();

	private final Map<Sneer1024, Party> _partiesByPublicKey = new HashMap<Sneer1024, Party>();



	private Sneer1024 createMickeyMouseKey() {
		String string = "" + System.currentTimeMillis() + System.nanoTime() + hashCode();
		return _crypto.sneer1024(string.getBytes());
	}

	@Override
	public Sneer1024 ownPublicKey() {
		return _ownKey;
	}

	@Override
	public Sneer1024 keyGiven(Contact contact) {
		return _keyByContact.get(contact);
	}

	@Override
	public Party partyGiven(Sneer1024 pk) {
		return partyGiven(pk, null);
	}

	@Override
	public synchronized Party partyGiven(Sneer1024 pk, Functor<Sneer1024, Party> factoryToUseIfAbsent) {
		Party result = _partiesByPublicKey.get(pk);
		if (result == null && factoryToUseIfAbsent != null) {
			result = factoryToUseIfAbsent.evaluate(pk);
			_partiesByPublicKey.put(pk, result);
		}
		return result;
	}

	@Override
	public synchronized Contact contactGiven(Sneer1024 peersPublicKey, Functor<Sneer1024, Contact> factoryToUseIfAbsent) {
		Contact result = contactGiven(peersPublicKey);
		if (result != null) return result;
		
		result = factoryToUseIfAbsent.evaluate(peersPublicKey);
		addKey(result, peersPublicKey);
		return result;
	}


	@Override
	public synchronized void addKey(Contact contact, Sneer1024 publicKey) {
		if(keyGiven(contact) != null) throw new IllegalArgumentException("There already was a public key registered for contact: " + contact.nickname().currentValue());
		_keyByContact.put(contact, publicKey);
	}


	@Override
	public synchronized Contact contactGiven(Sneer1024 peersPublicKey) {
		for (Contact candidate : _keyByContact.keySet())
			if(_keyByContact.get(candidate).equals(peersPublicKey))
				return candidate;
		
		return null;
	}


}
