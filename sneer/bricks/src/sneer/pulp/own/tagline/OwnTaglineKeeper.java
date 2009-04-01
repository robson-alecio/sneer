package sneer.pulp.own.tagline;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;

public interface OwnTaglineKeeper extends OldBrick {

	Signal<String> tagline();

	Consumer<String> taglineSetter();

}
