package sneer.bricks.pulp.reactive.gates.logic.impl;

import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.gates.logic.LogicGates;

public class LogicGatesImpl implements LogicGates {

	@Override
	public Signal<Boolean> and(Signal<Boolean> a, Signal<Boolean> b) {
		return new And(a, b).output();
	}

}
