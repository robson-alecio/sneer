package sneerapps.giventake.tests;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import functional.TestDashboard;

import sneer.bricks.things.Thing;

public class GiveNTakeTest {

	@Test
	public void testSimpleDeal() {
		if (!TestDashboard.newTestsShouldRun()) return;
		
		GiveNTakeUser _ana = new GiveNTakeUser();
		GiveNTakeUser _bob = new GiveNTakeUser();

		_ana.advertise("Lovely 3 room Apartment Pinheiros", "Lovely apartment in Pinheiros disctrict, São Paulo. 3 rooms. Kitchen. 150m2");
		Collection<Thing> found = _bob.search("apartment \"são paulo\"");
		
		Assert.assertSame(1, found.size());
	}
	
}
