package sneer.skin.sound.mic.tests;

import static wheel.lang.Environments.my;

import org.junit.Test;

import sneer.skin.sound.mic.Mic;
import tests.TestThatIsInjected;
import wheel.testutil.SignalUtils;

public class MicTest extends TestThatIsInjected {

	private static Mic _mic = my(Mic.class);
	
	@Test
	public void testIsRunningSignal() {
		SignalUtils.waitForValue(false, _mic.isRunning());
		
		_mic.open();
		SignalUtils.waitForValue(true, _mic.isRunning());
		
		_mic.close();
		SignalUtils.waitForValue(false, _mic.isRunning());
	}
}