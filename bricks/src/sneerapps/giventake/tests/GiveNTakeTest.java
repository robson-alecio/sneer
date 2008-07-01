package sneerapps.giventake.tests;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import sneer.bricks.things.Thing;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import testdashboard.TestDashboard;

public class GiveNTakeTest {

	@Test
	public void testSimpleDeal() {
		if (!TestDashboard.newTestsShouldRun()) return;
		
		Container containerA = ContainerUtils.newContainer();
		Container containerB = ContainerUtils.newContainer();
		GiveNTakeUser _ana = containerA.produce(GiveNTakeUser.class);
		GiveNTakeUser _bob = containerB.produce(GiveNTakeUser.class);

		_ana.advertise("Lovely 3 room Apartment Pinheiros", "Lovely apartment in Pinheiros disctrict, São Paulo. 3 rooms. Kitchen. 150m2");
		Collection<Thing> found = _bob.search("apartment \"são paulo\"");
		
		Assert.assertSame(1, found.size());
	}
	
}
