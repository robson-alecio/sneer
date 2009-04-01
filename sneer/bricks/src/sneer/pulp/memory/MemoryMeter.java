package sneer.pulp.memory;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.Signal;

public interface MemoryMeter extends OldBrick{
	
	Signal<Integer> usedMBs();
	Signal<Integer> usedMBsPeak();

	int maxMBs();
}