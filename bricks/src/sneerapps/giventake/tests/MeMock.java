package sneerapps.giventake.tests;

import java.util.Collection;
import java.util.HashSet;

import sneer.bricks.contacts.Contact;
import sneer.bricks.mesh.Me;
import sneer.bricks.mesh.Party;
import sneer.lego.Brick;
import sneerapps.giventake.GiveNTake;
import wheel.lang.Casts;
import wheel.reactive.lists.ListSignal;

public class MeMock implements Me {
	private Collection<GiveNTake> _counterparts = new HashSet<GiveNTake>();

	@Override
	public <B extends Brick> B brickProxyFor(Class<B> brickInterface) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public ListSignal<Contact> contacts() {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public Party navigateTo(Contact contact) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	public void addCounterpart(GiveNTake counterpart) {
		_counterparts.add(counterpart);
	}

	@Override
	public <B extends Brick> Collection<B> allImmediateContactBrickCounterparts(Class<B> class1) {
		return Casts.uncheckedGenericCast(_counterparts);
	}

}
