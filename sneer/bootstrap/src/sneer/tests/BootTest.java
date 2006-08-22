package sneer.tests;

import junit.framework.TestCase;

public class BootTest extends TestCase {

	private InstrumentedBoot _subject = new InstrumentedBoot();
;

	public void testStrapAuthentication() {
		byte[] wrongHash = new byte[]{1,2,3};
		_subject._expectedHash = wrongHash;
		try {
			_subject.runStrap();
			fail("Authentication should have failed.");
		} catch (Exception expected) {
			assertTrue(expected.getMessage().indexOf("tampered") > 0);
		}
	}
	
}
