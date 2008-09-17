package sneer.pulp.clock.impl;

import java.util.SortedSet;
import java.util.TreeSet;

import sneer.pulp.clock.Clock;
import wheel.lang.Threads;

class ClockImpl implements Clock {
	
	long _currentTimeMillis = 0;
	
	final SortedSet<Alarm> _alarms = new TreeSet<Alarm>();
	
	
	@Override
	synchronized public void addAlarm(int millisFromCurrentTime, Runnable runnable) {
		_alarms.add(new Alarm(runnable, millisFromCurrentTime, false));
	}

	@Override
	synchronized public void addPeriodicAlarm(int millisFromCurrentTime, Runnable runnable) {
		_alarms.add(new Alarm(runnable, millisFromCurrentTime, true));
	}

	@Override
	public void sleep(int millis) {
		Runnable notifier = createNotifier();
		synchronized (notifier) {
			addAlarm(millis, notifier);
			Threads.waitWithoutInterruptions(notifier);
		}
	}

	private Runnable createNotifier() {
		return new Runnable() { @Override synchronized public void run() {
			notify();
		}};
	}

	@Override
	synchronized public long time() {
		return _currentTimeMillis;
	}

	@Override
	synchronized public void advanceTime(int deltaMillis) {
		advanceTimeTo(_currentTimeMillis + deltaMillis);
	}

	
	@Override
	synchronized public void advanceTimeTo(long absoluteTimeMillis) {
		_currentTimeMillis = absoluteTimeMillis;
		wakeUpAlarmsIfNecessary();
	}
	
	private void wakeUpAlarmsIfNecessary() {
		
		while(_alarms.size()>0) {
			Alarm alarm = _alarms.first();
			
			if(!alarm.isTimeToWakeUp()) break;
			alarm.tryRunAndRemove();
			
			if(alarm.isPeriodic())
				_alarms.add(alarm);
		}
	}

	private class Alarm implements Comparable<Alarm>{
		
		final int _increment;
		
		long _wakeUpTime;
		final Runnable _runnable;

		Alarm(Runnable runnable, int millisFromCurrentTime, boolean isPeriodic) {
			_increment = isPeriodic ? millisFromCurrentTime : 0;
			_wakeUpTime = millisFromCurrentTime + _currentTimeMillis;
			_runnable = runnable;
		}
		
		public boolean isPeriodic() {
			return _increment>0;
		}

		boolean isTimeToWakeUp() {
			return _currentTimeMillis >= _wakeUpTime;
		}
		
		private void tryRunAndRemove(){
			_runnable.run();
			_alarms.remove(this);
			
			_wakeUpTime = _wakeUpTime+_increment;   //Periodic.incrementTime 
		}

		@Override
		public int compareTo(Alarm alarm) {
			return (int) (_wakeUpTime-alarm._wakeUpTime);
		}
		
		@Override
		public String toString() {
			return "Alarm: " + _wakeUpTime;
		}
	}

}