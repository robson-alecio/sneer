package sneer.pulp.own.tagline;

import sneer.brickness.Brick;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Signal;

@Brick
public interface OwnTaglineKeeper {

	Signal<String> tagline();

	Consumer<String> taglineSetter();

}
