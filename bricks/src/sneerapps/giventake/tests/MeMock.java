package sneerapps.giventake.tests;

import static wheel.lang.Types.cast;
import sneer.bricks.contacts.Contact;
import sneer.bricks.mesh.Me;
import sneer.bricks.mesh.Party;
import sneer.bricks.mesh.impl.BrickProxy;
import sneer.bricks.mesh.impl.SignalPublisher;
import sneer.lego.Brick;
import sneerapps.giventake.GiveNTake;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.sets.SetRegister;
import wheel.reactive.sets.SetSignal;
import wheel.reactive.sets.impl.SetRegisterImpl;

public class MeMock implements Me, SignalPublisher {
	private SetRegister<GiveNTake> _counterparts = new SetRegisterImpl<GiveNTake>();

	public void addCounterpart(GiveNTake gnt) {
		//_counterparts.add(wrapToSimulateRemoting(gnt));
		_counterparts.add(gnt);
	}

	@SuppressWarnings("unused")
	private <B extends Brick> B wrapToSimulateRemoting(B brick) {
		System.out.println("assuming only one brick counterpart for now");
		brick.toString();
		return cast(BrickProxy.createFor(GiveNTake.class, this));
	}

	@Override
	public <B extends Brick> SetSignal<B> allImmediateContactBrickCounterparts(Class<B> brickType) {
		return cast(_counterparts.output());
	}

	@Override
	public void subscribeTo(Class<? extends Brick> brickInterface, String signalName) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

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
}

