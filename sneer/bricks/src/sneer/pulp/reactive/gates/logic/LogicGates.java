package sneer.pulp.reactive.gates.logic;

import sneer.container.NewBrick;
import sneer.pulp.reactive.Signal;

@NewBrick
public interface LogicGates {

	Signal<Boolean> and(Signal<Boolean> a, Signal<Boolean> b);

}
