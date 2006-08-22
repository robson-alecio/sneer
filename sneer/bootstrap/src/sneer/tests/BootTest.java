package sneer.tests;

import junit.framework.TestCase;

public class BootTest extends TestCase {

	public void testStrapAuthentication() {
		try {
			new InstrumentedBoot().runStrap();
			fail("Authentication should have failed.");
		} catch (Exception expected) {
			assertTrue(expected.getMessage().indexOf("tampered") > 0);
		}
	}
	
}
