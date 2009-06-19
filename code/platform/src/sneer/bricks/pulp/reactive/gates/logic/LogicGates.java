package sneer.bricks.pulp.reactive.gates.logic;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface LogicGates {

	Signal<Boolean> and(Signal<Boolean> a, Signal<Boolean> b);

}
