package sneer.bricks.keymanager.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.contacts.Contact;
import sneer.bricks.crypto.Crypto;
import sneer.bricks.crypto.Sneer1024;
import sneer.bricks.keymanager.ContactAlreadyHadAKey;
import sneer.bricks.keymanager.KeyBelongsToOtherContact;
import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.mesh.Me;
import sneer.bricks.mesh.Party;
import sneer.lego.Inject;
import sneer.lego.Startable;
import wheel.lang.Functor;

public class KeyManagerImpl implements KeyManager, Startable {

	@Inject
	private Crypto _crypto; 
	
	private Sneer1024 _ownKey;
	
	private final Map<Contact, Sneer1024> _keyByContact = new HashMap<Contact, Sneer1024>();

	private final Map<Sneer1024, Party> _partiesByPublicKey = new HashMap<Sneer1024, Party>();

	@Override
	public void start() throws Exception {
		_ownKey = createMickeyMouseKey();
	}

	@Override
	public synchronized void addKey(Contact contact, Sneer1024 peersPublicKey) 
		throws ContactAlreadyHadAKey, KeyBelongsToOtherContact {
		
		if(_keyByContact.get(contact) != null)
			throw new ContactAlreadyHadAKey("the contact "+contact.nickname()+" has a key already");
			
		if(contactGiven(peersPublicKey) != null)
			throw new KeyBelongsToOtherContact("the key belongs to another contact");

		_keyByContact.put(contact, peersPublicKey);
	}

	private Sneer1024 createMickeyMouseKey() {
		String string = "" + System.currentTimeMillis() + System.nanoTime() + hashCode();
		return _crypto.sneer1024(string.getBytes());
	}

	@Override
	public synchronized Contact contactGiven(Sneer1024 peersPK) {
		for (Contact candidate : _keyByContact.keySet())
			if(_keyByContact.get(candidate).equals(peersPK))
				return candidate;

		return null;
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
	public synchronized Party partyGiven(Sneer1024 pk, Functor<Sneer1024, Party> factoryToUseIfAbsent) {
		Party result = _partiesByPublicKey.get(pk);
		if (result == null) {
			result = factoryToUseIfAbsent.evaluate(pk);
			_partiesByPublicKey.put(pk, result);
		}
		return result;
	}

}
