package sneer.pulp.memory;

import sneer.kernel.container.Brick;
import wheel.reactive.Signal;

public interface MemoryMeter extends Brick{
	
	Signal<Integer> currentMemory();
	Signal<Integer> maxUsedMemory();

	int totalMemory();
}