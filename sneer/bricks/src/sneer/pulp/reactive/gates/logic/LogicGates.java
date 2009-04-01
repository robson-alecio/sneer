package sneer.pulp.reactive.gates.logic;

import sneer.container.Brick;
import sneer.pulp.reactive.Signal;

@Brick
public interface LogicGates {

	Signal<Boolean> and(Signal<Boolean> a, Signal<Boolean> b);

}
