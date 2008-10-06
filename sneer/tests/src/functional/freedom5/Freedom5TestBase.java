package functional.freedom5;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import wheel.lang.Collections;
import wheel.lang.Threads;
import wheel.reactive.lists.ListSignal;
import functional.SovereignFunctionalTestBase;
import functional.SovereignParty;

public abstract class Freedom5TestBase extends SovereignFunctionalTestBase {
	
	@Test (timeout = 4000)
	public void shoutToTheWind() {
		
		SovereignParty c = _community.createParty("Cid");
		SovereignParty d = _community.createParty("Dan");
		
		_b.bidirectionalConnectTo(c);
		c.bidirectionalConnectTo(_a);
		c.bidirectionalConnectTo(d);
		
		_a.shout("A!!!");
		_b.shout("B!!!");
		c.shout("C!!!");
		d.shout("D!!!");

		waitForShoutsHeardBy(_a, "A!!!, B!!!, C!!!, D!!!");
		waitForShoutsHeardBy(_b, "A!!!, B!!!, C!!!, D!!!");
		waitForShoutsHeardBy(c, "A!!!, B!!!, C!!!, D!!!");
		waitForShoutsHeardBy(d, "A!!!, B!!!, C!!!, D!!!");
		
		//Implement: Measure traffic among peers
//		assertSame(8, abTraffic.currentValue());
//		assertSame(8, bcTraffic.currentValue());
//		assertSame(8, acTraffic.currentValue());
//		assertSame(8, cdTraffic.currentValue());
		
	}

	private void waitForShoutsHeardBy(SovereignParty user, String shoutsExpected) {
		while (true) {
			String heard = concat(user.shoutsHeard());
			if (shoutsExpected.equals(heard)) return;
			try {
				Threads.sleepWithoutInterruptions(10);
			} catch (RuntimeException ignored) {
				throw new IllegalStateException("Expected: " + shoutsExpected + "  was: " + heard);
			}
		}
	}
	
	private String concat(ListSignal<?> listSignal) {
		List<?> sorted = Collections.sortByToString(listSignal);
		return StringUtils.join(sorted, ", ");
	}


}