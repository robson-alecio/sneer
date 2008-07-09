package sneerapps.giventake.tests;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import sneer.bricks.things.Thing;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import wheel.reactive.sets.SetSignal;
import functional.SignalUtils;

public class GiveNTakeTest {

	@Ignore() //Klaus
	@Test (timeout = 3000)
	public void testSimpleDeal() {
		
		MeMock mock = new MeMock();
		Container containerA = ContainerUtils.newContainer(mock);
		Container containerB = ContainerUtils.newContainer(mock);

		GiveNTakeUser _ana = containerA.produce(GiveNTakeUser.class);
		GiveNTakeUser _bob = containerB.produce(GiveNTakeUser.class);
		
		_ana.connectTo(_bob);

		_bob.advertise("Playstation 3", "with sixaxis");
		SetSignal<Thing> found = _ana.search("sixaxis");
		SignalUtils.waitForValue(1, found.size());

		_ana.advertise("Lovely 3 room Apartment Pinheiros", "Lovely apartment in Pinheiros disctrict, São Paulo. 3 rooms. Kitchen. 150m2");
		found = _bob.search("apartment \"são paulo\"");
		SignalUtils.waitForValue(1, found.size());
		
		Assert.fail("Refactor BrickProxy into kernel.remoting");
	}
	
}
