package sneer.bricks.pulp.reactive.gates.numbers;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface NumberGates {

	Signal<Integer> add(Signal<Integer> a, Signal<Integer> b);
	
}
