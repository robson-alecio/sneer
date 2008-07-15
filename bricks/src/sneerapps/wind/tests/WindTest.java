package sneerapps.wind.tests;

import org.junit.Ignore;
import org.junit.Test;

import sneer.lego.ContainerUtils;
import wheel.reactive.sets.SetSignal;
import functional.SignalUtils;

public class WindTest {

	private SetSignal<String> _anaHeard;
	private SetSignal<String> _bobHeard;
	private SetSignal<String> _cidHeard;
	private SetSignal<String> _danHeard;

	@Ignore
	@Test (timeout = 4000)
	public void testShouting() {
		WindUser ana = createUser();
		WindUser bob = createUser();
		WindUser cid = createUser();
		WindUser dan = createUser();
		
		ana.connectTo(bob);
		ana.connectTo(cid);
		cid.connectTo(dan);
		dan.connectTo(ana);

		_anaHeard = ana.shoutsHeard();
		_bobHeard = bob.shoutsHeard();
		_cidHeard = cid.shoutsHeard();
		_danHeard = dan.shoutsHeard();

		waitForShoutsHeard(0);
		
		ana.shout("Sneer rulez!!!");
		waitForShoutsHeard(1);

		bob.shout("My son is born!!!");
		waitForShoutsHeard(2);

		cid.shout("Eco!!!");
		waitForShoutsHeard(3);

	}

	private void waitForShoutsHeard(int count) {
		SignalUtils.waitForValue(count, _anaHeard.size());
		SignalUtils.waitForValue(count, _bobHeard.size());
		SignalUtils.waitForValue(count, _cidHeard.size());
		SignalUtils.waitForValue(count, _danHeard.size());
	}

	private WindUser createUser() {
		return ContainerUtils.newContainer(new EnvironmentMock())
			.produce(WindUser.class);
	}
	
}
