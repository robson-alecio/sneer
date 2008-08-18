package sneer.pulp.ownTagline;

import sneer.lego.Brick;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface OwnTaglineKeeper extends Brick {

	Signal<String> tagline();

	Omnivore<String> taglineSetter();

}
