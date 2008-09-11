package sneer.pulp.clock.mocks.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.clock.mocks.ClockMock;

public class ClockMockTest extends TestThatIsInjected {


	final ClockMock clock = new ClockMock();
	int _lastRunned = 0;
	int _lastCount = 0;
	
	@Test
	public void test() throws Exception {
		
		clock.addAlarm(50, newEmptyRunnable(50));
		clock.addPeriodicAlarm(20, newEmptyRunnable(20));
		clock.addAlarm(10, newEmptyRunnable(10));
		clock.addAlarm(30, newEmptyRunnable(30));

		clock.advanceTime(1);	
		assertEquals(0,_lastRunned);
		assertEquals(0,_lastCount);
		
		clock.advanceTime(10);	
		assertEquals(10,_lastRunned);
		assertEquals(1,_lastCount);
		
		clock.advanceTime(20);	
		assertEquals(30,_lastRunned);
		assertEquals(1,_lastCount);
		
		clock.advanceTime(20);	
		assertEquals(50,_lastRunned);
		assertEquals(1,_lastCount);
		
		clock.advanceTime(1000);	
		assertEquals(20,_lastRunned);
		assertEquals(3,_lastCount);
		
	}

	private Runnable newEmptyRunnable(int timeout) {
		return new MyRunnable(timeout, this);
	}
	
	private static class MyRunnable implements Runnable{

		private final int _timeout;
		private final ClockMockTest _test;
		private int count = 0;

		public MyRunnable(int timeout, ClockMockTest test) {
			_timeout = timeout;
			_test = test;
		}

		@Override
		public void run() {
			_test._lastRunned = _timeout;
			_test._lastCount = ++count;
		}
	}
}
