package sneer.bricks.ownName.tests;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Test;

import sneer.bricks.contacts.Contact;
import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.keymanager.PublicKey;
import sneer.lego.Inject;
import sneer.lego.tests.BrickTestSupport;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

public class OwnNameTest extends BrickTestSupport {

	@Inject
	private KeyManager _keyManager;
	
	@Test
	public void testAddKey() throws Exception {
		Contact contact = newContact();

		byte[] keyBytes = new byte[128];
		Arrays.fill(keyBytes, (byte) 1);
		PublicKey key = _keyManager.unmarshall(keyBytes);
		assertNull(_keyManager.contactGiven(key));
		
		_keyManager.addKey(contact, key);
		assertSame(contact, _keyManager.contactGiven(key));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddKeyTwiceForSameContact() throws Exception {
		Contact contact = newContact();
		PublicKey key = _keyManager.unmarshall("random string".getBytes());
		
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
