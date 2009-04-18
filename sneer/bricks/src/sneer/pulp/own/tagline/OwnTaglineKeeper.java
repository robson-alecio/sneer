package sneer.pulp.own.tagline;

import sneer.brickness.OldBrick;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Signal;

public interface OwnTaglineKeeper extends OldBrick {

	Signal<String> tagline();

	Consumer<String> taglineSetter();

}
