package sneerapps.wind.tests;

import static org.junit.Assert.assertSame;
import static wheel.testutil.TestUtils.assertFloat;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import sneer.lego.ContainerUtils;
import wheel.lang.Collections;
import wheel.lang.FrozenTime;
import wheel.reactive.Signal;
import wheel.reactive.sets.SetSignal;

public class WindTest {

	@Ignore
	@Test //(timeout = 4000)
	public void testAffinity() {
		FrozenTime.freezeForCurrentThread(1);

		WindUser ana = createUser("Ana");
		WindUser bob = createUser("Bob");
		WindUser cid = createUser("Cid");
		
		Signal<Integer> abTraffic = ana.connectAndCountTrafficTo(bob);
		Signal<Integer> bcTraffic = bob.connectAndCountTrafficTo(cid);

		FrozenTime.freezeForCurrentThread(2);
		
		ana.setAffinityFor(bob, 90);
		bob.setAffinityFor(ana, 30);

		//bob.setAffinityFor(cid, ?); //Default: 10
		cid.setAffinityFor(bob, 70);
		
		assertFloat(90f, ana.affinityFor(bob));
		assertFloat(30f, bob.affinityFor(ana));
		assertFloat(10f, bob.affinityFor(cid));
		assertFloat(70f, cid.affinityFor(bob));

		assertFloat( 9f, ana.affinityFor(cid));
		assertFloat(21f, cid.affinityFor(ana));
	
		assertSame(-1, abTraffic.currentValue()); //Implement with correct value
		assertSame(-1, bcTraffic.currentValue()); //Implement with correct value
		
		Assert.fail("Add timeout to this test.");
	}

	@Ignore
	@Test //(timeout = 4000)
	public void testShouting() {
		WindUser ana = createUser("Ana");
		WindUser bob = createUser("Bob");
		WindUser cid = createUser("Cid");
		WindUser dan = createUser("Dan");
		
		Signal<Integer> abTraffic = ana.connectAndCountTrafficTo(bob);
		Signal<Integer> bcTraffic = bob.connectAndCountTrafficTo(cid);
		Signal<Integer> acTraffic = cid.connectAndCountTrafficTo(ana);
		Signal<Integer> cdTraffic = cid.connectAndCountTrafficTo(dan);

		ana.setAffinityFor(bob, 50);
		bob.setAffinityFor(ana, 50);

		//ana.affinityFor(cid, ?); Leave default affinity (10%)
		cid.setAffinityFor(ana, 30);

		cid.setAffinityFor(bob, 70);
		bob.setAffinityFor(cid, 90);
		
		cid.setAffinityFor(dan, 30);
		dan.setAffinityFor(cid, 90);

		ana.hearShoutsWithAffinityGreaterThan(12);
		bob.hearShoutsWithAffinityGreaterThan(40);
		cid.hearShoutsWithAffinityGreaterThan(60);
		dan.hearShoutsWithAffinityGreaterThan(80);
		
		ana.shout("A!!!");
		bob.shout("B!!!");
		cid.shout("C!!!");
		dan.shout("D!!!");

		assertShoutsHeardBy(ana, "A!!!, B!!!, C!!!, D!!!");
		assertShoutsHeardBy(bob, "A!!!, B!!!, C!!!");
		assertShoutsHeardBy(cid, "B!!!, C!!!");
		assertShoutsHeardBy(dan, "C!!!, D!!!");
		
		assertSame(6, abTraffic.currentValue());
		assertSame(5, bcTraffic.currentValue());
		assertSame(2, acTraffic.currentValue());
		assertSame(4, cdTraffic.currentValue());
		
		Assert.fail("Add timeout to this test.");
	}

	private void assertShoutsHeardBy(WindUser user, String shoutsExpected) {
		Assert.assertEquals(shoutsExpected, concat(user.shoutsHeard()));
	}

	private String concat(SetSignal<?> setSignal) {
		List<?> sorted = Collections.sortByToString(setSignal);
		return StringUtils.join(sorted, ", ");
	}



	private WindUser createUser(String name) {
		WindUser result = ContainerUtils.newContainer()
			.produce(WindUser.class);
		result.name(name);
		return result;
	}
	
}
