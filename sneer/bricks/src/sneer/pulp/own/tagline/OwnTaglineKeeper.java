package sneer.pulp.own.tagline;

import sneer.kernel.container.Brick;
import wheel.lang.Consumer;
import wheel.reactive.Signal;

public interface OwnTaglineKeeper extends Brick {

	Signal<String> tagline();

	Consumer<String> taglineSetter();

}
