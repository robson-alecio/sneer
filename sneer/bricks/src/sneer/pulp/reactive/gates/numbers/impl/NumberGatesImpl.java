package sneer.pulp.reactive.gates.numbers.impl;

import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.gates.numbers.NumberGates;

public class NumberGatesImpl implements NumberGates {

	@Override
	public Signal<Integer> add(Signal<Integer> a, Signal<Integer> b) {
		return new Adder(a, b).output();
	}

}
