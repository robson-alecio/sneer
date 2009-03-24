package sneer.pulp.reactive.gates.numbers;

import sneer.brickness.Brick;
import sneer.pulp.reactive.Signal;

public interface NumberGates extends Brick {

	Signal<Integer> add(Signal<Integer> a, Signal<Integer> b);
	
}
