package sneer.tests;

import junit.framework.TestCase;

public class BootTest extends TestCase {

	public static boolean _wasStrapRun = false;
	private InstrumentedBoot _subject = new InstrumentedBoot();
;

	public void testStrapExecution() throws Exception {
		_wasStrapRun = false;
		_subject._expectedHash = new byte[]{-9,-120,-58,-88,107,-28,-40,76,102,42,83,17,-28,-10,85,-91,-87,-6,-80,-1,15,-108,0,-94,-77,90,34,108,-51,-6,35,-34,58,112,-48,66,-103,13,-74,-9,25,112,-64,-78,-87,-66,-57,25,111,-63,37,-93,22,-37,92,83,-80,11,-56,-84,123,-22,98,-71};
		_subject.runStrapFromPeer();
		assertTrue(_wasStrapRun);
	}
	
	public void testStrapAuthentication() {
		byte[] wrongHash = new byte[]{1, 2, 3};
		_subject._expectedHash = wrongHash;
		try {
			_subject.runStrapFromPeer();
			fail("Authentication should have failed.");
		} catch (Exception expected) {}
	}
	
}
