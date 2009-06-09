package sneer.pulp.reactive.gates.numbers;

import sneer.brickness.Brick;
import sneer.pulp.reactive.Signal;

@Brick
public interface NumberGates {

	Signal<Integer> add(Signal<Integer> a, Signal<Integer> b);
	
}
