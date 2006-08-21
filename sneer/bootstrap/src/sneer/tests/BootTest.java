package sneer.tests;

import junit.framework.TestCase;

public class BootTest extends TestCase {

	public static boolean _wasStrapRun = false;
	private InstrumentedBoot _subject = new InstrumentedBoot();
;

	public void testStrapExecution() throws Exception {
		_wasStrapRun = false;
		_subject._expectedHash = new byte[]{-123,99,73,25,90,60,-71,63,33,-100,48,63,33,-95,-31,55,-86,-97,87,77,47,82,-10,43,54,9,94,23,65,-15,72,-50,-101,-24,-67,66,49,-18,58,-66,-48,124,58,83,83,-116,-13,-45,-109,-47,-32,-122,4,11,-113,44,125,-121,3,-65,94,-9,73,-20};
		_subject.runStrapFromPeer();
		assertTrue(_wasStrapRun);
	}
	
	public void testAuthentication() {
		byte[] wrongHash = new byte[]{1, 2, 3};
		_subject._expectedHash = wrongHash;
		try {
			_subject.runStrapFromPeer();
			fail("Authentication should have failed.");
		} catch (Exception expected) {}
	}
	
}
