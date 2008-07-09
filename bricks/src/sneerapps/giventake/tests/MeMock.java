package sneerapps.giventake.tests;

import static wheel.lang.Types.uncheckedGenericCast;

import java.util.Collection;
import java.util.HashSet;

import sneer.bricks.contacts.Contact;
import sneer.bricks.mesh.Me;
import sneer.bricks.mesh.Party;
import sneer.bricks.mesh.impl.BrickProxy;
import sneer.bricks.mesh.impl.SignalPublisher;
import sneer.lego.Brick;
import sneerapps.giventake.GiveNTake;
import wheel.reactive.lists.ListSignal;

public class MeMock implements Me, SignalPublisher {
	private Collection<GiveNTake> _counterparts = new HashSet<GiveNTake>();

	@Override
	public <B extends Brick> B brickProxyFor(Class<B> brickInterface) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListSignal<Contact> contacts() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Party navigateTo(Contact contact) {
		throw new UnsupportedOperationException();
	}

	public void addCounterpart(GiveNTake gnt) {
		_counterparts.add(wrapToSimulateRemoting(gnt));
	}

	private <B extends Brick> B wrapToSimulateRemoting(B brick) {
		System.out.println("assuming only on brick counterpart for now");
		brick.toString();
		return uncheckedGenericCast(BrickProxy.createFor(GiveNTake.class, this));
	}

	@Override
	public <B extends Brick> Collection<B> allImmediateContactBrickCounterparts(Class<B> brickType) {
		return uncheckedGenericCast(_counterparts);
	}

	@Override
	public void subscribeTo(Class<? extends Brick> brickInterface, String signalName) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

}
