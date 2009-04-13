package functional.freedom2;

import org.junit.Test;

import sneer.pulp.reactive.Signal;
import wheel.testutil.SignalUtils;
import functional.SovereignFunctionalTestBase;
import functional.SovereignParty;

public abstract class Freedom2TestBase extends SovereignFunctionalTestBase {

	@Test (timeout = 1000)
	public void testRemoteNameChange() {

		SignalUtils.waitForValue("Ana Almeida", b().navigateAndGetName("Ana Almeida"));

		SignalUtils.waitForValue("Bruno Barros", a().navigateAndGetName("Bruno Barros"));

		b().setOwnName("B. Barros");
		SignalUtils.waitForValue("B. Barros", a().navigateAndGetName("Bruno Barros"));

		b().setOwnName("Dr Barros");
		SignalUtils.waitForValue("Dr Barros", a().navigateAndGetName("Bruno Barros"));
	}

	@Test (timeout = 10000)
	public void testNicknames() {
		SovereignParty c = createParty("Carla Costa");
		SovereignParty d = createParty("Denis Dalton");
		
		a().bidirectionalConnectTo(c);
		b().bidirectionalConnectTo(c);
		c.bidirectionalConnectTo(d);

		SignalUtils.waitForValue("Carla Costa", b().navigateAndGetName("Carla Costa"));

		a().giveNicknameTo(b(), "Bruno");
		b().giveNicknameTo(a(), "Aninha");
		a().giveNicknameTo(c, "Carla");
		c.giveNicknameTo(d, "Dedé");
		
		SignalUtils.waitForValue("Bruno Barros", a().navigateAndGetName("Bruno"));
		SignalUtils.waitForValue("Ana Almeida", b().navigateAndGetName("Carla Costa/Ana Almeida"));
		SignalUtils.waitForValue("Ana Almeida", a().navigateAndGetName("Bruno/Aninha"));
		SignalUtils.waitForValue("Bruno Barros", a().navigateAndGetName("Bruno/Aninha/Bruno"));
		SignalUtils.waitForValue("Denis Dalton", a().navigateAndGetName("Bruno/Carla Costa/Dedé"));

		Signal<String> anasName = b().navigateAndGetName("Carla Costa/Ana Almeida");
		b().giveNicknameTo(c, "Carlinha");
		c.giveNicknameTo(a(), "Aninha");
		SignalUtils.waitForValue("Ana Almeida", b().navigateAndGetName("Carlinha/Aninha"));
		
		a().setOwnName("Dr Ana");
		SignalUtils.waitForValue("Dr Ana", anasName);
	}


}
