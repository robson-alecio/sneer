package sneer.pulp.clock.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import sneer.pulp.clock.Clock;
import wheel.lang.Threads;

class ClockImpl implements Clock {
	
	long _currentTimeMillis = 0;
	
	final Set<Alarm> _alarms = new TreeSet<Alarm>();
	
	
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
		checkTime();
	}
	
	private void checkTime() {

		List<Alarm> tmpPeriodics = new ArrayList<Alarm>();
		Iterator<Alarm> iterator = _alarms.iterator();
			
		while(iterator.hasNext()) {
			Alarm alarm = iterator.next();
			
			if(alarm.isPeriodic()) //Store Periodic
				tmpPeriodics.add(alarm);
			
			if(!alarm.tryRunAndRemove(iterator)) //Break Last Timeout
				break;				
		}
		
		_alarms.addAll(tmpPeriodics);
		
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
		
		boolean tryRunAndRemove(Iterator<Alarm> iterator){
			if(_currentTimeMillis <= _nextAlarmAbsoluteTimeMillis )
				return false;
			
			_runnable.run();
			iterator.remove();
			
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