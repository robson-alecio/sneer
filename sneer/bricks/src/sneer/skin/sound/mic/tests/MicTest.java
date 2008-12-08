package sneer.skin.sound.mic.tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sneer.skin.sound.mic.Mic;
import tests.TestThatIsInjected;
import wheel.lang.Consumer;
import wheel.lang.Threads;

import static wheel.lang.Environments.my;

public class MicTest extends TestThatIsInjected {

	private static Mic _mic = my(Mic.class);
	
	private final Object _monitor = new Object();

	private final List<Boolean> _recorded = new ArrayList<Boolean>();
	private final Consumer<Boolean> _recorder = new Consumer<Boolean>(){ @Override public void consume(Boolean value) {
		_recorded.add(value);
		synchronized (_monitor) {
			_monitor.notify();
		}
	}};
	
	
	@Test
	public void testIsRunningSignal() {
		
		_mic.isRunning().addReceiver(_recorder);
		
		_mic.open();
		whait();
		
		_mic.close();
		whait();
		
		expectOutputSequence(false, true, false);
		
	}

	private void whait() {
		synchronized (_monitor) {
			Threads.waitWithoutInterruptions(_monitor);
		}
	}
	
	private void expectOutputSequence(boolean... expected) {
		assertEquals(expected.length, _recorded.size());
		
		for (int i = 0; i < expected.length; i++)
			assertEquals(expected[i], _recorded.get(i)); 
	}	
}