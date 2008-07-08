package sneerapps.giventake.tests;

import org.junit.Assert;
import org.junit.Test;

import sneer.bricks.mesh.Me;
import sneer.bricks.things.Thing;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.lego.impl.SimpleBinder;
import wheel.reactive.sets.SetSignal;
import functional.SignalUtils;

public class GiveNTakeTest {

	@Test (timeout = 3000)
	public void testSimpleDeal() {
		
		SimpleBinder binder = new SimpleBinder();
		binder.bind(Me.class).to(MeMock.class);
		Container containerA = ContainerUtils.newContainer(binder);
		Container containerB = ContainerUtils.newContainer(binder);

		GiveNTakeUser _ana = containerA.produce(GiveNTakeUser.class);
		GiveNTakeUser _bob = containerB.produce(GiveNTakeUser.class);
		
		_ana.connectTo(_bob);

		_bob.advertise("Playstation 3", "with sixaxis");
		SetSignal<Thing> found = _ana.search("sixaxis");
		SignalUtils.waitForValue(1, found.size());

		_ana.advertise("Lovely 3 room Apartment Pinheiros", "Lovely apartment in Pinheiros disctrict, São Paulo. 3 rooms. Kitchen. 150m2");
		found = _bob.search("apartment \"são paulo\"");
		SignalUtils.waitForValue(1, found.size());

		Assert.fail("Declare GiveNTakeUserImpl._me as MeMock and fix container. It is creating a new MeMock instead os injecting the one it already has.");
	}
	
}
