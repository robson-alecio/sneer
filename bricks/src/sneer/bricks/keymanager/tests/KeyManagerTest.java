package sneer.bricks.keymanager.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import sneer.bricks.contacts.Contact;
import sneer.bricks.keymanager.KeyManager;
import sneer.lego.Inject;
import sneer.lego.tests.BrickTestSupport;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

public class KeyManagerTest extends BrickTestSupport {

	@Inject
	private KeyManager _keyManager;
	
	@Test
	public void testOwnKey() throws Exception {
		byte[] pk = _keyManager.ownPublicKey();
		assertEquals("1234", new String(pk));
	}

	@Test
	public void testAddKey() throws Exception {
		Contact contact = newContact();
		byte[] key = "some key".getBytes();
		
		assertNull(_keyManager.contactGiven(key));
		
		_keyManager.addKey(contact, key);
		assertSame(contact, _keyManager.contactGiven(key));
	}

	@Test(expected = IllegalStateException.class)
	public void testAddKeyTwiceForSameContact() throws Exception {
		Contact contact = newContact();
		byte[] key = "some key".getBytes();
		
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
