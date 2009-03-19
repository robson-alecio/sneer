package sneer.pulp.own.tagline;

import sneer.brickness.Brick;
import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;

public interface OwnTaglineKeeper extends Brick {

	Signal<String> tagline();

	Consumer<String> taglineSetter();

}
