package sneer.bricks.pulp.own.tagline;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface OwnTaglineKeeper {

	Signal<String> tagline();

	Consumer<String> taglineSetter();

}
