package sneer.bricks.ownName;

import sneer.lego.Brick;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface OwnNameKeeper extends Brick {

	Signal<String> name();

	Omnivore<String> nameSetter();

	void setName(String name);

	String getName();
}
