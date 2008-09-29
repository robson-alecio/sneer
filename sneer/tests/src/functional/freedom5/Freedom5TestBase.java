package functional.freedom5;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import wheel.lang.Collections;
import wheel.reactive.lists.ListSignal;
import functional.SovereignFunctionalTestBase;
import functional.SovereignParty;

public abstract class Freedom5TestBase extends SovereignFunctionalTestBase {
	
	@Ignore
	@Test (timeout = 1000)
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

		assertShoutsHeardBy(_a, "A!!!, B!!!, C!!!, D!!!");
		assertShoutsHeardBy(_b, "A!!!, B!!!, C!!!, D!!!");
		assertShoutsHeardBy(c, "A!!!, B!!!, C!!!, D!!!");
		assertShoutsHeardBy(d, "A!!!, B!!!, C!!!, D!!!");
		
		//Implement: Measure traffic among peers
//		assertSame(8, abTraffic.currentValue());
//		assertSame(8, bcTraffic.currentValue());
//		assertSame(8, acTraffic.currentValue());
//		assertSame(8, cdTraffic.currentValue());
		
	}

	private void assertShoutsHeardBy(SovereignParty user, String shoutsExpected) {
		shoutsExpected.toString();
		Assert.assertEquals(shoutsExpected, concat(user.shoutsHeard()));
	}
	
	private String concat(ListSignal<?> listSignal) {
		List<?> sorted = Collections.sortByToString(listSignal);
		return StringUtils.join(sorted, ", ");
	}


}