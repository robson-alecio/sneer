package sneer.tests.freedom2;

import org.junit.Ignore;
import org.junit.Test;

import sneer.tests.SovereignFunctionalTestBase;
import sneer.tests.SovereignParty;

public abstract class Freedom2TestBase extends SovereignFunctionalTestBase {

	@Test (timeout = 3000)
	public void isOnline() {
		assertTrue(a().isOnline("Bruno Barros"));
		assertTrue(b().isOnline("Ana Almeida"));
	}

	
	@Ignore
	@Test (timeout = 1000)
	public void testRemoteNameChange() {
		b().navigateAndWaitForName("Ana Almeida", "Ana Almeida");
		a().navigateAndWaitForName("Bruno Barros", "Bruno Barros");

		b().setOwnName("BB");
		a().navigateAndWaitForName("Bruno Barros", "BB");

		b().setOwnName("Dr Barros");
		a().navigateAndWaitForName("Bruno Barros", "Dr Barros");
	}

	@Ignore
	@Test (timeout = 10000)
	public void testNicknames() {
		SovereignParty c = createParty("Carla Costa");
		SovereignParty d = createParty("Denis Dalton");

		a().bidirectionalConnectTo(c);
		b().bidirectionalConnectTo(c);
		c.bidirectionalConnectTo(d);

		b().navigateAndWaitForName("Carla Costa", "Carla Costa");

		a().giveNicknameTo(b(), "Bruno");
		b().giveNicknameTo(a(), "Aninha");
		a().giveNicknameTo(c, "Carla");
		c.giveNicknameTo(d, "Dedé");

		a().navigateAndWaitForName("Bruno", "Bruno Barros");
		b().navigateAndWaitForName("Carla Costa/Ana Almeida", "Ana Almeida");
		a().navigateAndWaitForName("Bruno/Aninha", "Ana Almeida");
		a().navigateAndWaitForName("Bruno/Aninha/Bruno", "Bruno Barros");
		a().navigateAndWaitForName("Bruno/Carla Costa/Dedé", "Denis Dalton");

		b().giveNicknameTo(c, "Carlinha");
		c.giveNicknameTo(a(), "Aninha");
		b().navigateAndWaitForName("Carlinha/Aninha", "Ana Almeida");
	}
}