package sneer.bricks.hardware.timer.tests;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.timer.Timer;
import sneer.foundation.brickness.testsupport.BrickTest;

public class TimerTest extends BrickTest {

	private final Clock _clock = my(Clock.class);
	private final Timer _subject = my(Timer.class);
	private StringBuilder _events = new StringBuilder();
	
	@Test
	public void testAlarms() throws Exception {
		final List<Integer> _order = new ArrayList<Integer>();
		
		_subject.wakeUpInAtLeast(50, new Worker(50, _order));
		_subject.wakeUpEvery(20, new Worker(20, _order));
		_subject.wakeUpInAtLeast(10, new Worker(10, _order));
		_subject.wakeUpEvery(35, new Worker(35, _order));
		_subject.wakeUpInAtLeast(30, new Worker(30,_order));
		
		_clock.advanceTime(81);
		assertTrue(81 == _clock.time().currentValue());
		
		Integer lastInteger = null;
		for (Integer timeout : _order) {
			if(lastInteger!=null)
				assertTrue(timeout>=lastInteger);
			lastInteger = timeout;
		}
	}

	@Test
	public void testSimultaneousAlarms() throws Exception {
		final List<Integer> _order = new ArrayList<Integer>();
		
		_subject.wakeUpInAtLeast(10, new Worker(10, _order));
		_subject.wakeUpInAtLeast(10, new Worker(10, _order));
		
		_clock.advanceTime(10);
		
		assertEquals(2, _order.size());
	}

	
	private class Worker implements Steppable, Runnable {

		private final int _timeout;
		private final List<Integer> _order;
		private int _count = 0;

		public Worker(int timeout, List<Integer> order) {
			_timeout = timeout;
			_order = order;
		}

		@Override
		public boolean step() {
			_count++;
			_order.add(_timeout * _count);
			return true;
		}

		@Override
		public void run() {
			step();
		}
	}
	
	@Test
	public void testAlarmThatAddsAlarm() throws Exception {
		_subject.wakeUpInAtLeast(1, new Runnable(){ @Override public void run() {
			_events.append("first");
			_subject.wakeUpInAtLeast(1, new Runnable(){ @Override public void run() {
				_events.append("second");
			}});
		}});
		
		_clock.advanceTime(2);
		assertEvents("first");

		_clock.advanceTime(1);
		assertEvents("second");
	}

	private void assertEvents(String expected) {
		assertEquals(expected, _events.toString());
		_events = new StringBuilder();
	}

}
