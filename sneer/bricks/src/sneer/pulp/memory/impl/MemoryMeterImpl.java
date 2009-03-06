package sneer.pulp.memory.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.clock.Clock;
import sneer.pulp.memory.MemoryMeter;
import sneer.pulp.threadpool.Stepper;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

class MemoryMeterImpl implements MemoryMeter {

	static private final int PERIOD_IN_MILLIS = 2000;
	static private final Runtime RUNTIME = Runtime.getRuntime();

	private final Register<Integer> _usedMBs = new RegisterImpl<Integer>(0);
	private final Register<Integer> _usedMBsPeak = new RegisterImpl<Integer>(0);
	
	private final Clock _clock = my(Clock.class); {
		_clock.wakeUpNowAndEvery(PERIOD_IN_MILLIS, new Stepper() { @Override public boolean step() {
			measureMemory();
			return true;
		}});
	}
	
	@Override	public Signal<Integer> usedMBs() { return _usedMBs.output(); }
	@Override	public Signal<Integer> usedMBsPeak() { return _usedMBsPeak.output(); }

	@Override
	public int maxMBs() {
		return toMBs(RUNTIME.maxMemory());
	}

	private void measureMemory() {
		int used = measureUsedMBs();
		setUsed(used);
		if (used > peak()) setPeak(used);
	}

	private int measureUsedMBs() {
		long total, total2, free;
		do {
			total = RUNTIME.totalMemory();
			free = RUNTIME.freeMemory();
			total2 = RUNTIME.totalMemory();
		} while (total != total2);
			
		return toMBs(total - free);
	}

	
	private void setPeak(int used) {
		_usedMBsPeak.setter().consume(used);
	}

	private Integer peak() {
		return _usedMBsPeak.output().currentValue();
	}

	private void setUsed(int current) {
		_usedMBs.setter().consume(current);
	}

	private int toMBs(long bytes) {
		return (int)(bytes / (1024 * 1024)); 
	}

}