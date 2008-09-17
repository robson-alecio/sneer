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
			
			if(!alarm.tryRunAndRemove())
				break;				
			
			if(alarm.isPeriodic())
				_alarms.add(alarm);
		}
	}

	private class Alarm implements Comparable<Alarm>{
		
		final int _increment;
		
		long _nextAlarmAbsoluteTimeMillis;
		final Runnable _runnable;

		Alarm(Runnable runnable, int millisFromCurrentTime, boolean isPeriodic) {
			_increment = isPeriodic ? millisFromCurrentTime : 0;
			_nextAlarmAbsoluteTimeMillis = millisFromCurrentTime + _currentTimeMillis;
			_runnable = runnable;
		}
		
		public boolean isPeriodic() {
			return _increment>0;
		}
		
		boolean tryRunAndRemove(){
			if(_currentTimeMillis <= _nextAlarmAbsoluteTimeMillis )
				return false;
			
			_runnable.run();
			_alarms.remove(this);
			
			if(_increment==0)  
				return true; //NotPeriodic
				
			_nextAlarmAbsoluteTimeMillis = _nextAlarmAbsoluteTimeMillis+_increment;   //Periodic.incrementTime 
			return true;
		}

		@Override
		public int compareTo(Alarm alarm) {
			return (int) (_nextAlarmAbsoluteTimeMillis-alarm._nextAlarmAbsoluteTimeMillis);
		}
		
		@Override
		public String toString() {
			return "Alarm: " + _nextAlarmAbsoluteTimeMillis;
		}
	}

}