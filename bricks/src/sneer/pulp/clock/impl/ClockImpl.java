package sneer.pulp.clock.impl;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import sneer.pulp.clock.Clock;
import wheel.lang.Threads;

class ClockImpl implements Clock {
	
	long _currentTimeMilles = 0;
	
	final Set<Alarm> _alarms = new TreeSet<Alarm>();
	
	
	@Override
	public void addAlarm(int millisFromCurrentTime, Runnable runnable) {
		_alarms.add(new Alarm(runnable, millisFromCurrentTime, false));
	}

	@Override
	public void addPeriodicAlarm(int millisFromCurrentTime, Runnable runnable) {
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
	public long time() {
		return _currentTimeMilles;
	}

	@Override
	public void advanceTime(int deltaMillis) {
		_currentTimeMilles = _currentTimeMilles + deltaMillis;
		checkTime();
	}
	
	private void checkTime() {
		boolean tryAgain = true;

		while(tryAgain){
			Iterator<Alarm> iterator = _alarms.iterator();
			
			while(iterator.hasNext()) {
				Alarm alarm = iterator.next();
				
				tryAgain = alarm.tryRunAndRemove(iterator);
				
				if(!tryAgain) //Break Last Timeout
					break;				

				if(alarm._increment>0){ //Break Periodic
					break;
				}
				
				tryAgain=false;
			}
		}
	}

	private class Alarm implements Comparable<Alarm>{
		
		final int _increment;
		
		long _nextAlarmAbsoluteTimeMillies;
		final Runnable _runnable;

		Alarm(Runnable runnable, int millisFromCurrentTime, boolean isPeriodic) {
			_increment = isPeriodic ? millisFromCurrentTime : 0;
			_nextAlarmAbsoluteTimeMillies = millisFromCurrentTime + _currentTimeMilles;
			_runnable = runnable;
		}
		
		boolean tryRunAndRemove(Iterator<Alarm> iterator){
			if(_currentTimeMilles <= _nextAlarmAbsoluteTimeMillies )
				return false;
			
			_runnable.run();
			iterator.remove();
			
			if(_increment==0)  
				return true; //NotPeriodic
				
			_nextAlarmAbsoluteTimeMillies = _nextAlarmAbsoluteTimeMillies+_increment;   //Periodic.incrementTime 
			_alarms.add(this);
			return true;
		}

		@Override
		public int compareTo(Alarm alarm) {
			return (int) (_nextAlarmAbsoluteTimeMillies-alarm._nextAlarmAbsoluteTimeMillies);
		}
		
		@Override
		public String toString() {
			return "Alarm: " + _nextAlarmAbsoluteTimeMillies;
		}
	}
}