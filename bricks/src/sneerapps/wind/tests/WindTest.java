package sneerapps.wind.tests;

import org.junit.Test;

import sneer.lego.ContainerUtils;
import sneerapps.wind.Shout;
import wheel.reactive.sets.SetSignal;
import functional.SignalUtils;

public class WindTest {

	private SetSignal<Shout> _anaHeard;
	private SetSignal<Shout> _bobHeard;
	private SetSignal<Shout> _cidHeard;
	private SetSignal<Shout> _danHeard;

	@Test (timeout = 5000)
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
		waitForShoutHeard("Sneer rulez!!!", ana);

		bob.shout("My son is born!!!");
		waitForShoutsHeard(2);

		cid.shout("Eco!!!");
		waitForShoutsHeard(3);
		waitForShoutHeard("Eco!!!", cid);

		dan.shout("Geronimo!!!");
		waitForShoutsHeard(4);
		waitForShoutHeard("Sneer rulez!!!", ana);
		waitForShoutHeard("My son is born!!!", bob);
		waitForShoutHeard("Eco!!!", cid);
		waitForShoutHeard("Geronimo!!!", dan);

	}

	private void waitForShoutHeard(String phrase, WindUser user) {
		Shout expected = new Shout(phrase, user.publicKey());
		SignalUtils.waitForElement(expected, _anaHeard);
		SignalUtils.waitForElement(expected, _bobHeard);
		SignalUtils.waitForElement(expected, _cidHeard);
		SignalUtils.waitForElement(expected, _danHeard);
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
