package sneer.pulp.memory.impl;

import static java.lang.System.gc;
import sneer.kernel.container.Inject;
import sneer.pulp.memory.MemoryMeter;
import sneer.pulp.threadpool.Stepper;
import sneer.pulp.threadpool.ThreadPool;
import wheel.lang.Threads;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

class MemoryMeterImpl implements MemoryMeter{
	
	private final Sentinel _sentinel = new Sentinel();
	private final Register<Integer> _currentMemory = new RegisterImpl<Integer>(0);
	private final Register<Integer> _maxUsedMemory = new RegisterImpl<Integer>(0);
	
	@Inject
	static private ThreadPool _threads; {
		_threads.registerStepper(_sentinel);
	}
	
	@Override
	public Signal<Integer> currentMemory() {
		return _currentMemory.output();
	}

	@Override
	public Signal<Integer> maxUsedMemory() {
		return _maxUsedMemory.output();
	}

	@Override
	public int totalMemory() {
		return (int)Runtime.getRuntime().maxMemory() / (1024 * 1024);
	}
	
	private final class Sentinel implements Stepper{
		
		private final int PERIOD_IN_MILLIS = 2000;
		private int _lastUsedMBs = 0;
		
		@Override
		public boolean step() {
			notifyAnySignificantMemoryUsageChange();
			Threads.sleepWithoutInterruptions(PERIOD_IN_MILLIS);
			return true;
		}

		private void notifyAnySignificantMemoryUsageChange() {
			if (!isSignificant()) return;
			gc();
			if (!isSignificant()) return;
			_lastUsedMBs = usedMBs();
			checkMax(_lastUsedMBs);
			_currentMemory.setter().consume(_lastUsedMBs);
		}

		private void checkMax(int current) {
			if(current>_maxUsedMemory.output().currentValue())
				_maxUsedMemory.setter().consume(current);
		}

		private boolean isSignificant() {
			return usedMBs() - _lastUsedMBs != 0;
		}

		private int usedMBs() {
			return (int)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
		}
	}
}