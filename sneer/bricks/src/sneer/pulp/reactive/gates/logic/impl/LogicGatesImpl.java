package sneer.pulp.reactive.gates.logic.impl;

import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.gates.logic.LogicGates;

public class LogicGatesImpl implements LogicGates {

	@Override
	public Signal<Boolean> and(Signal<Boolean> a, Signal<Boolean> b) {
		return new And(a, b).output();
	}

}
