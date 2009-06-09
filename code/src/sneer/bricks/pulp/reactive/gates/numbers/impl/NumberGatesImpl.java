package sneer.bricks.pulp.reactive.gates.numbers.impl;

import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.gates.numbers.NumberGates;

class NumberGatesImpl implements NumberGates {

	@Override
	public Signal<Integer> add(Signal<Integer> a, Signal<Integer> b) {
		return new Adder(a, b).output();
	}

}
