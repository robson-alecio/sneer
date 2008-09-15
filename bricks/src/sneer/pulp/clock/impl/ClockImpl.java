package sneer.pulp.clock.impl;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import sneer.pulp.clock.Clock;
import wheel.lang.Threads;

class ClockImpl implements Clock {
	
	long _currentTime = 0;
	
	final Set<Alarm> _alarms = new TreeSet<Alarm>();
	
	
	@Override
	public void addAlarm(int millisFromNow, Runnable runnable) {
		_alarms.add(new Alarm(runnable, millisFromNow, false));
	}

	@Override
	public void addPeriodicAlarm(int millis, Runnable runnable) {
		_alarms.add(new Alarm(runnable, millis, true));
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
		return _currentTime;
	}

	@Override
	public void advanceTime(int deltaMillis) {
		_currentTime = _currentTime + deltaMillis;
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
			}
		}
	}

	private class Alarm implements Comparable<Alarm>{
		
		final int _increment;
		
		int _millisFromNow;
		final Runnable _runnable;

		Alarm(Runnable runnable, int millisFromNow, boolean isPeriodic) {
			_increment = isPeriodic ? millisFromNow : 0;
			_millisFromNow = millisFromNow;
			_runnable = runnable;
		}
		
		boolean tryRunAndRemove(Iterator<Alarm> iterator){
			System.err.println("Sandro, fix this please: currentTime is absolute, millisFromNow is relative.");
			if(_currentTime <= _millisFromNow )
				return false;
			
			_runnable.run();
			iterator.remove();
			
			if(_increment==0)  
				return true; //NotPeriodic
				
			_millisFromNow = _millisFromNow+_increment;   //Periodic.incrementTime 
			_alarms.add(this);
			return true;
		}

		@Override
		public int compareTo(Alarm alarm) {
			return _millisFromNow-alarm._millisFromNow;
		}
		
		@Override
		public String toString() {
			return "Alarm: " + _millisFromNow;
		}
	}
}