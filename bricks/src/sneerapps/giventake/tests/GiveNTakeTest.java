package sneerapps.giventake.tests;

import org.junit.Test;

import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.pulp.things.Thing;
import wheel.reactive.sets.SetSignal;
import functional.SignalUtils;

public class GiveNTakeTest {

	@Test (timeout = 3000)
	public void testSimpleDeal() {
		
		Container containerA = ContainerUtils.newContainer(new MeMock());
		Container containerB = ContainerUtils.newContainer(new MeMock());

		GiveNTakeUser _ana = containerA.produce(GiveNTakeUser.class);
		GiveNTakeUser _bob = containerB.produce(GiveNTakeUser.class);
		
		_ana.connectTo(_bob);

		_bob.advertise("Playstation 3", "with sixaxis");
		SetSignal<Thing> found = _ana.search("sixaxis");
		SignalUtils.waitForValue(1, found.size());

		_ana.advertise("Lovely 3 room Apartment Pinheiros", "Lovely apartment in Pinheiros disctrict, São Paulo. 3 rooms. Kitchen. 150m2");
		found = _bob.search("apartment \"são paulo\"");
		SignalUtils.waitForValue(1, found.size());
	}
	
}
