package sneer.tests.freedom5;

import org.junit.Test;

import sneer.foundation.brickness.testsupport.Intermittent;
import sneer.tests.SovereignFunctionalTestBase;
import sneer.tests.SovereignParty;


public abstract class Freedom5TestBase extends SovereignFunctionalTestBase {

	@Intermittent
	@Test (timeout = 10000)
	public void shoutToTheWind() {
		
		SovereignParty c = createParty("Cid");
		SovereignParty d = createParty("Dan");
		
		connect(b(), c  );
		connect(c  , a());
		connect(c  , d  );
		
		a().shout("A!!!");
		b().shout("B!!!");
		c  .shout("C!!!");
		d  .shout("D!!!");

		a().waitForShouts("A!!!, B!!!, C!!!, D!!!");
		b().waitForShouts("A!!!, B!!!, C!!!, D!!!");
		c  .waitForShouts("A!!!, B!!!, C!!!, D!!!");
		d  .waitForShouts("A!!!, B!!!, C!!!, D!!!");
		
		//Implement: Measure traffic among peers
//		assertSame(8, abTraffic.currentValue());
//		assertSame(8, bcTraffic.currentValue());
//		assertSame(8, acTraffic.currentValue());
//		assertSame(8, cdTraffic.currentValue());
		
	}
	

	@Test (timeout=5000)
	public void canHearPastShouts() {
		
		a().shout("A!!!");
		b().shout("B!!!");
		
		SovereignParty c = createParty("Cid");
		connect(c, b());

		c.waitForShouts("A!!!, B!!!");
	}

}