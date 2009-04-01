package sneer.pulp.reactive.gates.numbers;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.Signal;

public interface NumberGates extends OldBrick {

	Signal<Integer> add(Signal<Integer> a, Signal<Integer> b);
	
}
