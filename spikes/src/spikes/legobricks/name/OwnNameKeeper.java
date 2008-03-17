package spikes.legobricks.name;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface OwnNameKeeper {

	Signal<String> name();

	Omnivore<String> nameSetter();

}
