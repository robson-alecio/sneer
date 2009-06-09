package sneer.bricks.pulp.own.name;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.contacts.Contact;
import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface OwnNameKeeper {

	Signal<String> name();

	Consumer<String> nameSetter();

	Signal<String> nameOf(Contact contact);

}
