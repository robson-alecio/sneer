package sneer.bricks.keymanager.tests;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.junit.Test;

import sneer.bricks.contacts.Contact;
import sneer.bricks.crypto.Crypto;
import sneer.bricks.crypto.Sneer1024;
import sneer.bricks.keymanager.KeyManager;
import sneer.lego.Inject;
import sneer.lego.tests.BrickTestSupport;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

public class KeyManagerTest extends BrickTestSupport {

	@Inject
	private KeyManager _keyManager;
	
	@Inject
	private Crypto _crypto;
	
	@Test
	public void testAddKey() throws Exception {
		Contact contact = newContact();
		Sneer1024 key = _crypto.sneer1024("random string".getBytes());
		
		assertNull(_keyManager.contactGiven(key));
		
		_keyManager.addKey(contact, key);
		assertSame(contact, _keyManager.contactGiven(key));
	}

	@Test(expected = IllegalStateException.class)
	public void testAddKeyTwiceForSameContact() throws Exception {
		Contact contact = newContact();
		Sneer1024 key = _crypto.sneer1024("random string".getBytes());
		
		assertNull(_keyManager.contactGiven(key));

		_keyManager.addKey(contact, key);
		assertSame(contact, _keyManager.contactGiven(key));
		
		_keyManager.addKey(contact, key);
		fail("Should have thrown exception");
	}

	private Contact newContact() {
		Contact contact = new Contact() { @Override public Signal<String> nickname() {
			Register<String> r = new RegisterImpl<String>("Klaus");
			return r.output(); 
		}};
		return contact;
	}
	
}
