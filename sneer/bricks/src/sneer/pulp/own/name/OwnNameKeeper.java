package sneer.pulp.own.name;

import sneer.brickness.Brick;
import sneer.container.NewBrick;
import sneer.pulp.contacts.Contact;
import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;

@NewBrick
public interface OwnNameKeeper extends Brick {

	Signal<String> name();

	Consumer<String> nameSetter();

	Signal<String> nameOf(Contact contact);

}
