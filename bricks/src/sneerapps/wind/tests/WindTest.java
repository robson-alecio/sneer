package sneerapps.wind.tests;

import static org.junit.Assert.assertSame;

import org.junit.Test;

import sneer.lego.ContainerUtils;
import wheel.lang.FrozenTime;
import wheel.reactive.Signal;

public class WindTest {

	@Test //(timeout = 4000)
	public void testShouting() {
		WindUser ana = createUser("Ana");
		WindUser bob = createUser("Bob");
		WindUser cid = createUser("Cid");
		WindUser dan = createUser("Dan");
		
		FrozenTime.freezeForCurrentThread(1);
		
		Signal<Integer> abTraffic = ana.connectAndCountTrafficTo(bob);
		Signal<Integer> bcTraffic = bob.connectAndCountTrafficTo(cid);
		Signal<Integer> acTraffic = cid.connectAndCountTrafficTo(ana);
		Signal<Integer> cdTraffic = cid.connectAndCountTrafficTo(dan);

		ana.shout("A!!!");
		bob.shout("B!!!");
		cid.shout("C!!!");
		dan.shout("D!!!");

		assertShoutsHeardBy(ana, "A!!!, B!!!, C!!!, D!!!");
		assertShoutsHeardBy(bob, "A!!!, B!!!, C!!!");
		assertShoutsHeardBy(cid, "B!!!, C!!!");
		assertShoutsHeardBy(dan, "C!!!, D!!!");
		
		//Fix: Put correct values below. These are just regression values.
		assertSame(8, abTraffic.currentValue());
		assertSame(8, bcTraffic.currentValue());
		assertSame(8, acTraffic.currentValue());
		assertSame(8, cdTraffic.currentValue());
		
	}

	private void assertShoutsHeardBy(WindUser user, String shoutsExpected) {
		user.toString();
		shoutsExpected.toString();
		//Fix: Uncomment below.
		//Assert.assertEquals(shoutsExpected, concat(user.shoutsHeard()));
	}
//	private String concat(SetSignal<?> setSignal) {
//		List<?> sorted = Collections.sortByToString(setSignal);
//		return StringUtils.join(sorted, ", ");
//	}



	private WindUser createUser(String name) {
		WindUser result = ContainerUtils.newContainer()
			.produce(WindUser.class);
		result.name(name);
		return result;
	}
	
}
