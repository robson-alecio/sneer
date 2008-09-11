package sneer.pulp.clock.impl.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.clock.Clock;

public class ClockTest extends TestThatIsInjected {

	int _lastRunned = 0;
	int _lastCount = 0;
	List<Integer> _order = new ArrayList<Integer>();
	
	@Inject
	static private Clock clock;
	
	@Test
	public void test() throws Exception {
		
		clock.addAlarm(50, newEmptyRunnable(50));
		clock.addPeriodicAlarm(20, newEmptyRunnable(20));
		clock.addAlarm(10, newEmptyRunnable(10));
		clock.addPeriodicAlarm(35, newEmptyRunnable(35));
		clock.addAlarm(30, newEmptyRunnable(30));

		step(1,0,0); 		//time = 01 []
		step(11, 10, 1); 	//time = 11 [10]
		step(20, 30, 1); 	//time = 31 [10, 20, 30]
		step(20, 50, 1); 	//time = 51 [10, 20, 30, 35, 40, 50]
		step(30, 20, 4); 	//time = 81 [10, 20, 30, 35, 40, 50, 60, 70, 80]
	}

	@Test
	public void testCallTime() {
		Integer lastInteger = null;
		for (Integer timeout : _order) {
			if(lastInteger!=null)
				assertTrue(timeout>=lastInteger);
			lastInteger = timeout;
		}
	}

	private void step(int plusTime, int time, int count) {
		clock.advanceTime(plusTime);	
		assertEquals(time, _lastRunned);
		assertEquals(count, _lastCount);
	}
	
	private Runnable newEmptyRunnable(int timeout) {
		return new MyRunnable(timeout, this);
	}
	
	private static class MyRunnable implements Runnable{

		private final int _timeout;
		private final ClockTest _test;
		private int count = 0;

		public MyRunnable(int timeout, ClockTest test) {
			_timeout = timeout;
			_test = test;
		}

		@Override
		public void run() {
			_test._lastRunned = _timeout;
			_test._lastCount = ++count;
			_test._order.add(_timeout * _test._lastCount);
		}
	}
}
