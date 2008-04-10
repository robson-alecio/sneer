package sneer.bricks.keymanager.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import sneer.bricks.contacts.Contact;
import sneer.bricks.keymanager.ContactAlreadyHadAKey;
import sneer.bricks.keymanager.KeyBelongsToOtherContact;
import sneer.bricks.keymanager.KeyManager;

public class KeyManagerImpl implements KeyManager {

	//Fix: read read from local file
	private Key _ownKey = new Key("1234");
	
	private Map<Contact,Key> _keyByContact = new HashMap<Contact, Key>(); 
	
	@Override
	public synchronized void addKey(Contact contact, byte[] peersPublicKey) 
		throws ContactAlreadyHadAKey, KeyBelongsToOtherContact {
		
		Key key = _keyByContact.get(contact);
		
		if(key != null)
			throw new ContactAlreadyHadAKey("the contact "+contact.nickname()+" has a key already");
			
		Contact other = contactGiven(peersPublicKey);
		if(other != null)
			throw new KeyBelongsToOtherContact("the key [TODO: the key] belongs to another contact");

		//Fix: store key file
		_keyByContact.put(contact, new Key(peersPublicKey));
	}

	@Override
	public synchronized Contact contactGiven(byte[] peersPublicKey) {
		Key wanted = new Key(peersPublicKey);
		for (Contact contact : _keyByContact.keySet()) {
			Key key = _keyByContact.get(contact);
			if(key.equals(wanted))
				return contact;
		}
		return null;
	}

	@Override
	public byte[] ownPublicKey() {
		return _ownKey.data();
	}
}

class Key {

	private byte[] _data;
	
	public Key(String s) {
		_data = s.getBytes();
	}

	public Key(byte[] bytes) {
		_data = bytes;
	}

	public byte[] data() {
		return _data;
	}
	
	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Key)) return false;
		
		Key other = (Key) obj;
		return Arrays.equals(_data, other.data());
	}
}