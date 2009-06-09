package sneer.functionaltests.freedom2;

import static sneer.foundation.commons.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.SignalUtils;
import sneer.functionaltests.SovereignFunctionalTestBase;
import sneer.functionaltests.SovereignParty;

public abstract class Freedom2TestBase extends SovereignFunctionalTestBase {

	private static final SignalUtils SIGNAL_UTILS = my(SignalUtils.class);

	@Test (timeout = 1000)
	public void testRemoteNameChange() {
		SIGNAL_UTILS.waitForValue("Ana Almeida", b().navigateAndGetName("Ana Almeida"));
		SIGNAL_UTILS.waitForValue("Bruno Barros", a().navigateAndGetName("Bruno Barros"));

		b().setOwnName("B. Barros");
		SIGNAL_UTILS.waitForValue("B. Barros", a().navigateAndGetName("Bruno Barros"));

		b().setOwnName("Dr Barros");
		SIGNAL_UTILS.waitForValue("Dr Barros", a().navigateAndGetName("Bruno Barros"));
	}

	@Test (timeout = 10000)
	public void testNicknames() {
		SovereignParty c = createParty("Carla Costa");
		SovereignParty d = createParty("Denis Dalton");

		a().bidirectionalConnectTo(c);
		b().bidirectionalConnectTo(c);
		c.bidirectionalConnectTo(d);

		SIGNAL_UTILS.waitForValue("Carla Costa", b().navigateAndGetName("Carla Costa"));

		a().giveNicknameTo(b(), "Bruno");
		b().giveNicknameTo(a(), "Aninha");
		a().giveNicknameTo(c, "Carla");
		c.giveNicknameTo(d, "Dedé");

		SIGNAL_UTILS.waitForValue("Bruno Barros", a().navigateAndGetName("Bruno"));
		SIGNAL_UTILS.waitForValue("Ana Almeida", b().navigateAndGetName("Carla Costa/Ana Almeida"));
		SIGNAL_UTILS.waitForValue("Ana Almeida", a().navigateAndGetName("Bruno/Aninha"));
		SIGNAL_UTILS.waitForValue("Bruno Barros", a().navigateAndGetName("Bruno/Aninha/Bruno"));
		SIGNAL_UTILS.waitForValue("Denis Dalton", a().navigateAndGetName("Bruno/Carla Costa/Dedé"));

		Signal<String> anasName = b().navigateAndGetName("Carla Costa/Ana Almeida");
		b().giveNicknameTo(c, "Carlinha");
		c.giveNicknameTo(a(), "Aninha");
		SIGNAL_UTILS.waitForValue("Ana Almeida", b().navigateAndGetName("Carlinha/Aninha"));

		a().setOwnName("Dr Ana");
		SIGNAL_UTILS.waitForValue("Dr Ana", anasName);
	}
}