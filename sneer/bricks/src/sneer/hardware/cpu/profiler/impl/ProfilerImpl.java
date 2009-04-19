package sneer.hardware.cpu.profiler.impl;

import sneer.hardware.cpu.profiler.Profiler;
import sneer.pulp.logging.Logger;
import static sneer.commons.environments.Environments.my;


class ProfilerImpl implements Profiler {

	private class Worker {

		private long _timeEntered;
		private long _timeExited = -1;

		void enter() {
			_timeEntered = now();
			if (_timeExited == -1) return;
			
			addTimeOutside(_timeEntered - _timeExited);
		}

		void exit() {
			_timeExited = now();
			addTimeInside(_timeExited - _timeEntered);
		}

	}

	private final String _name;
	
	private long _totalTimeOutside = 0;
	private long _totalTimeInside = 0;

	private long _lastLogTime = 0;

	private final ThreadLocal<Worker> _worker = new ThreadLocal<Worker>();

	public ProfilerImpl(String name) {
		_name = name;
	}

	public void enter() {
		worker().enter();
	}

	private Worker worker() {
		if (_worker.get() == null)
			_worker.set(new Worker());
		
		return _worker.get();
	}

	public void exit() {
		worker().exit();
		
		logOnceInAWhile();
	}

	synchronized private void addTimeOutside(long timeOutside) {
		_totalTimeOutside += timeOutside;
	}

	synchronized private void addTimeInside(long timeInside) {
		_totalTimeInside += timeInside;
	}

	synchronized private void logOnceInAWhile() {
		if (_totalTimeOutside == 0) return;

		if (now() - _lastLogTime < 1000L * 1000 * 1000 * 30) return;
		_lastLogTime = now();
		
		my(Logger.class).log("{} is running during {}% of the time", _name, percentageInside());
	}

	private long percentageInside() {
		return 100 * _totalTimeInside / (_totalTimeInside + _totalTimeOutside);
	}

	private long now() {
		return System.nanoTime();
	}

}
