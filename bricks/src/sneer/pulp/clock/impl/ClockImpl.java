package sneer.pulp.clock.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sneer.kernel.container.Inject;
import sneer.pulp.clock.Clock;
import sneer.pulp.clock.realtime.RealtimeClock;
import wheel.lang.Threads;

class ClockImpl implements Clock {
	
	@Inject
	private static RealtimeClock _realtime;

	long _currentTime = _realtime.currentTimeMillis();
	
	final List<Alarm> _alarms = new ArrayList<Alarm>();
	
	ClockImpl() {
		//Fix
		Threads.startDaemon("Clock", new Runnable() { @Override public void run() {
			while (true) {
				if(_currentTime != _realtime.currentTimeMillis()){
					_currentTime = _realtime.currentTimeMillis();
					checkTime();
				}
				Threads.sleepWithoutInterruptions(1);
			}
		}}); 
	}

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
		Object monitor = new Object();
		synchronized (monitor) {
			addAlarm(millis, notifier(monitor));
			Threads.waitWithoutInterruptions(monitor);
		}
	}

	private Runnable notifier(final Object monitor) {
		return new Runnable() { @Override public void run() {
			synchronized (monitor) {
				monitor.notify();
			}
		}};
	}

	@Override
	public long time() {
		return _currentTime;
	}

	private void checkTime() {
		int i = 1;
		while(i>0){
			Collections.sort(_alarms, 
				new Comparator<Alarm>(){@Override public int compare(Alarm alarm0, Alarm alarm1) {
					return alarm0._millisFromNow - alarm1._millisFromNow;
				}}
			);
			
			List<Alarm> tmp = new ArrayList<Alarm>(_alarms);
			
			for (i = 0; i < tmp.size(); i++) {
				Alarm alarm = tmp.get(i);
				if(!alarm.tryRun()) //Break Last Timeout
					break;
				
				if(alarm._increment>0){ //Break Periodic
					i=1;
					break;
				}
			}
		}
	}

	private class Alarm{
		
		final int _increment;
		
		int _millisFromNow;
		final Runnable _runnable;

		Alarm(Runnable runnable, int millisFromNow, boolean isPeriodic) {
			_increment = isPeriodic ? millisFromNow : 0;
			_millisFromNow = millisFromNow;
			_runnable = runnable;
		}
		
		boolean tryRun(){
			System.err.println("Sandro, fix this please: currentTime is absolute, millisFromNow is relative.");
			if(_currentTime <= _millisFromNow )
				return false;
			
			_runnable.run();
			
			if(_increment==0) _alarms.remove(this); //NotPeriodic.remove
			else _millisFromNow = _millisFromNow+_increment;   //Periodic.incrementTime 
			
			return true;
		}
	}
}