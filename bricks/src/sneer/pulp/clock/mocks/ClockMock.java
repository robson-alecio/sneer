package sneer.pulp.clock.mocks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sneer.pulp.clock.Clock;

public class ClockMock implements Clock {

	private final List<Runnable> _alarms = new ArrayList<Runnable>();
	private final Set<Runnable> _periodicAlarms = new HashSet<Runnable>();

	@Override
	public void addAlarm(int millisFromNow, Runnable runnable) {
		_alarms.add(runnable);
	}

	@Override
	public void addPeriodicAlarm(int millis, Runnable runnable) {
		_alarms.add(runnable);
		_periodicAlarms.add(runnable);
	}

	@Override
	public void sleep(int millis) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public long time() {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Deprecated //Tests that use this break the encapsulation of the bricks they are testing. Use something like ClockMock.advanceTime(millis). 
	public void triggerAlarm(int alarmIndex) {
		Runnable alarm = _alarms.get(alarmIndex);
		if (!_periodicAlarms.contains(alarm))
			_alarms.remove(alarmIndex);
		alarm.run();
	}

}
