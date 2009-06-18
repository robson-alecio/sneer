package sneer.bricks.pulp.memory.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.pulp.memory.MemoryMeter;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.threads.Stepper;

class MemoryMeterImpl implements MemoryMeter {

	static private final int PERIOD_IN_MILLIS = 2000;
	static private final Runtime RUNTIME = Runtime.getRuntime();

	private final Register<Integer> _usedMBs = my(Signals.class).newRegister(0);
	private final Register<Integer> _usedMBsPeak = my(Signals.class).newRegister(0);
	
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