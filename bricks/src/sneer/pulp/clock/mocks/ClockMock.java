package sneer.pulp.clock.mocks;

import java.util.ArrayList;
import java.util.List;

import sneer.pulp.clock.Clock;

public class ClockMock implements Clock {

	private final List<Runnable> _alarms = new ArrayList<Runnable>();

	@Override
	public void setAlarm(int millis, Runnable runnable) {
		_alarms .add(runnable);
	}

	@Override
	public void sleep(int millis) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public long time() {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	public void triggerAlarm(int alarmIndex) {
		_alarms.get(alarmIndex).run();
	}

}
