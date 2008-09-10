package sneer.pulp.clock.mocks;

import java.util.ArrayList;
import java.util.List;

import sneer.pulp.clock.Alarm;
import sneer.pulp.clock.Clock;

public class ClockMock implements Clock {

	private final List<AlarmMock> _alarms = new ArrayList<AlarmMock>();

	@Override
	public Alarm setAlarm(int millis, Runnable runnable) {
		final AlarmMock alarm = new AlarmMock(runnable);
		_alarms.add(alarm);
		return alarm;
	}

	@Override
	public void sleep(int millis) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public long time() {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	public Alarm triggerAlarm(int alarmIndex) {
		final AlarmMock alarm = _alarms.get(alarmIndex);
		alarm.run();
		return alarm;
	}

}
