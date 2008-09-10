package sneer.pulp.clock.mocks;

import sneer.pulp.clock.Alarm;

public class AlarmMock implements Alarm, Runnable {

	private final Runnable _runnable;
	private boolean _isOn = true;

	public AlarmMock(Runnable runnable) {
		_runnable = runnable;
	}

	@Override
	public boolean isOn() {
		return _isOn;
	}

	@Override
	public void turnOff() {
		_isOn = false;
	}

	@Override
	public void run() {
		_runnable.run();
	}
}
