package sneer.pulp.clock.mocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sneer.pulp.clock.Clock;

public class ClockMock implements Clock {

	int _currentTime = 0;
	final List<Tupla> _alarms = new ArrayList<Tupla>();

	@Override
	public void addAlarm(int millisFromNow, Runnable runnable) {
		_alarms.add(new Tupla(this, runnable, millisFromNow, false));
	}

	@Override
	public void addPeriodicAlarm(int millis, Runnable runnable) {
		_alarms.add(new Tupla(this, runnable, millis, true));
	}

	@Override
	public void sleep(int millis) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public long time() {
		return _currentTime;
	}

	public void checkTime() {
		Collections.sort(_alarms, 
			new Comparator<Tupla>(){@Override public int compare(Tupla tupla0, Tupla tupla1) {
				return tupla0._millisFromNow - tupla1._millisFromNow;
			}});
		
		List<Tupla> tmp = new ArrayList<Tupla>(_alarms);
		
		
		for (Tupla tupla : tmp)
			if(!tupla.tryRun())	return;
	}

	public void advanceTime(int plusTime) {
		_currentTime = _currentTime+plusTime;
		checkTime();
	}
}

class Tupla{
	
	final int _increment;
	
	int _millisFromNow;
	final Runnable _runnable;

	private final ClockMock _clockMock;

	Tupla(ClockMock clockMock, Runnable runnable, int millisFromNow, boolean isPeriodic) {
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