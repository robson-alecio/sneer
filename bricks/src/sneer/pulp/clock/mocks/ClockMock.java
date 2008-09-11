package sneer.pulp.clock.mocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sneer.pulp.clock.Clock;

public class ClockMock implements Clock {

	int _currentTime = 0;
	final List<Alarm> _alarms = new ArrayList<Alarm>();

	@Override
	public void addAlarm(int millisFromNow, Runnable runnable) {
		_alarms.add(new Alarm(this, runnable, millisFromNow, false));
	}

	@Override
	public void addPeriodicAlarm(int millis, Runnable runnable) {
		_alarms.add(new Alarm(this, runnable, millis, true));
	}

	@Override
	public void sleep(int millis) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
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
				}});
			
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

	public void advanceTime(int plusTime) {
		_currentTime = _currentTime+plusTime;
		checkTime();
	}
}

class Alarm{
	
	final int _increment;
	
	int _millisFromNow;
	final Runnable _runnable;

	private final ClockMock _clockMock;

	Alarm(ClockMock clockMock, Runnable runnable, int millisFromNow, boolean isPeriodic) {
		_clockMock = clockMock;
		_increment = isPeriodic ? millisFromNow : 0;
		_millisFromNow = millisFromNow;
		_runnable = runnable;
	}
	
	boolean tryRun(){
		if(_clockMock._currentTime <= _millisFromNow )
			return false;
		
		_runnable.run();
		
		if(_increment==0) _clockMock._alarms.remove(this); //NotPeriodic.remove
		else _millisFromNow = _millisFromNow+_increment;   //Periodic.incrementTime 
		
		return true;
	}
}