package sneer.bricks.pulp.own.name;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface OwnNameKeeper {

	Signal<String> name();

	Consumer<String> nameSetter();

}
