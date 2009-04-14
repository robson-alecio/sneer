package functional.freedom5;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import sneer.pulp.reactive.collections.ListSignal;

import wheel.lang.Collections;
import functional.SovereignFunctionalTestBase;
import functional.SovereignParty;

public abstract class Freedom5TestBase extends SovereignFunctionalTestBase {
	
	@Test (timeout = 5000)
	public void shoutToTheWind() {
		
		SovereignParty c = createParty("Cid");
		SovereignParty d = createParty("Dan");
		
		b().bidirectionalConnectTo(c);
		c.bidirectionalConnectTo(a());
		c.bidirectionalConnectTo(d);
		
		a().shout("A!!!");
		b().shout("B!!!");
		c.shout("C!!!");
		d.shout("D!!!");

		waitForShoutsHeardBy(a(), "A!!!, B!!!, C!!!, D!!!");
		waitForShoutsHeardBy(b(), "A!!!, B!!!, C!!!, D!!!");
		waitForShoutsHeardBy(c, "A!!!, B!!!, C!!!, D!!!");
		waitForShoutsHeardBy(d, "A!!!, B!!!, C!!!, D!!!");
		
		//Implement: Measure traffic among peers
//		assertSame(8, abTraffic.currentValue());
//		assertSame(8, bcTraffic.currentValue());
//		assertSame(8, acTraffic.currentValue());
//		assertSame(8, cdTraffic.currentValue());
		
	}
	

	@Test(timeout=6000)
	public void canHearPastShouts() {
		
		a().shout("A!!!");
		b().shout("B!!!");
		
		SovereignParty c = createParty("Cid");
		c.bidirectionalConnectTo(b());

		waitForShoutsHeardBy(c, "A!!!, B!!!");
	}

	private void waitForShoutsHeardBy(SovereignParty user, String shoutsExpected) {
		while (true) {
			String heard = concat(user.shoutsHeard());
			if (shoutsExpected.equals(heard)) return;
			try {
				Thread.sleep(10);
			} catch (InterruptedException ignored) {
				throw new RuntimeException("Interrupted while waiting for: " + shoutsExpected + "  was still: " + heard);
			}
		}
	}
	
	private String concat(ListSignal<?> listSignal) {
		List<?> sorted = Collections.sortByToString(listSignal);
		return StringUtils.join(sorted, ", ");
	}
}