package sneer.pulp.memory;

import sneer.brickness.Brick;
import wheel.reactive.Signal;

public interface MemoryMeter extends Brick{
	
	Signal<Integer> usedMBs();
	Signal<Integer> usedMBsPeak();

	int maxMBs();
}