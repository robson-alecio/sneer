package sneer.pulp.reactive.gates.logic;

import sneer.pulp.reactive.Signal;

public interface LogicGates {

	Signal<Boolean> and(Signal<Boolean> a, Signal<Boolean> b);

}
