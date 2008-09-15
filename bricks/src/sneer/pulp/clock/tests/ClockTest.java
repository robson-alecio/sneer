package sneer.pulp.clock.tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.clock.Clock;

public class ClockTest extends TestThatIsInjected {

	@Inject
	static private Clock _subject;
	
	@Test
	public void testAlarms() throws Exception {
		final List<Integer> _order = new ArrayList<Integer>();
		
		_subject.addAlarm(50, new MyRunnable(50, _order));
		_subject.addPeriodicAlarm(20, new MyRunnable(20, _order));
		_subject.addAlarm(10, new MyRunnable(10, _order));
		_subject.addPeriodicAlarm(35, new MyRunnable(35, _order));
		_subject.addAlarm(30, new MyRunnable(30,_order));
		
		Integer lastInteger = null;
		for (Integer timeout : _order) {
			if(lastInteger!=null)
				assertTrue(timeout>=lastInteger);
			lastInteger = timeout;
		}
	}

	private class MyRunnable implements Runnable{

		private final int _timeout;
		private final List<Integer> _order;
		private int _count = 0;

		public MyRunnable(int timeout, List<Integer> order) {
			_timeout = timeout;
			_order = order;
		}

		@Override
		public void run() {
			_count++;
			_order.add(_timeout * _count);
		}
	}
}
