package sneer.pulp.clock.impl.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.clock.Clock;
import sneer.pulp.clock.realtime.mocks.BrokenClock;

public class ClockTest {

	int _lastRunned = 0;
	int _lastCount = 0;
	
	final List<Integer> _order = new ArrayList<Integer>();
	final BrokenClock mock = new BrokenClock();
	
	@Test
	public void test() throws Exception {

		final Container container = ContainerUtils.newContainer(mock);
		final Clock clock = container.produce(Clock.class);

		clock.addAlarm(50, new MyRunnable(50));
		clock.addPeriodicAlarm(20, new MyRunnable(20));
		clock.addAlarm(10, new MyRunnable(10));
		clock.addPeriodicAlarm(35, new MyRunnable(35));
		clock.addAlarm(30, new MyRunnable(30));

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
		mock.advanceTime(plusTime);	
		assertEquals(time, _lastRunned);
		assertEquals(count, _lastCount);
	}
	
	private class MyRunnable implements Runnable{

		private final int _timeout;
		private int count = 0;

		public MyRunnable(int timeout) {
			_timeout = timeout;
		}

		@Override
		public void run() {
			_lastRunned = _timeout;
			_lastCount = ++count;
			_order.add(_timeout * _lastCount);
		}
	}
}
