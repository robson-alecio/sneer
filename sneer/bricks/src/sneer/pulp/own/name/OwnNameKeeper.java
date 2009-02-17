package sneer.pulp.own.name;

import sneer.kernel.container.Brick;
import sneer.pulp.contacts.Contact;
import wheel.lang.Consumer;
import wheel.reactive.Signal;

public interface OwnNameKeeper extends Brick {

	Signal<String> name();

	Consumer<String> nameSetter();

	Signal<String> nameOf(Contact contact);

}
