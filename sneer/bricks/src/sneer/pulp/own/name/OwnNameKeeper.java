package sneer.pulp.own.name;

import sneer.brickness.OldBrick;
import sneer.container.Brick;
import sneer.pulp.contacts.Contact;
import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;

@Brick
public interface OwnNameKeeper extends OldBrick {

	Signal<String> name();

	Consumer<String> nameSetter();

	Signal<String> nameOf(Contact contact);

}
