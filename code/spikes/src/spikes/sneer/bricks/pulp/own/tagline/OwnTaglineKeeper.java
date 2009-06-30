package spikes.sneer.bricks.pulp.own.tagline;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface OwnTaglineKeeper {

	Signal<String> tagline();

	Consumer<String> taglineSetter();

}
