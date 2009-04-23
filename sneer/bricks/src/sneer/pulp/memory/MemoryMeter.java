package sneer.pulp.memory;

import sneer.brickness.Brick;
import sneer.pulp.reactive.Signal;

@Brick
public interface MemoryMeter {
	
	Signal<Integer> usedMBs();
	Signal<Integer> usedMBsPeak();

	int maxMBs();
}