package sneer.bricks.hardware.ram.meter;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface MemoryMeter {
	
	Signal<Integer> usedMBs();
	Signal<Integer> usedMBsPeak();

	int maxMBs();
}