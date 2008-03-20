package functional;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public abstract class Freedom2Test extends SovereignFunctionalTest {

	
	@Test
	public void testNicknames() {
		
		//if (!TestDashboard.newTestsShouldRun()) return;
		
		SovereignParty a = _community.createParty("Ana Almeida");
		SovereignParty b = _community.createParty("Bruno Barros");
		SovereignParty c = _community.createParty("Carla Costa");
		SovereignParty d = _community.createParty("Denis Dalton");
		
		a.bidirectionalConnectTo(b);
		a.bidirectionalConnectTo(c);
		b.bidirectionalConnectTo(c);
		c.bidirectionalConnectTo(d);
		
		a.giveNicknameTo(b, "Bruno");
		b.giveNicknameTo(a, "Aninha");
		a.giveNicknameTo(c, "Carla");
		c.giveNicknameTo(a, "Miguxa");
		c.giveNicknameTo(d, "Dedé");
		
		assertSame("Bruno Barros", a.navigateAndGetName("Bruno"));
		assertSame("Ana Almeida", b.navigateAndGetName("Carla Costa/Miguxa"));
		assertSame("Ana Almeida", a.navigateAndGetName("Bruno/Aninha"));
		assertSame("Bruno Barros", a.navigateAndGetName("Bruno/Aninha/Bruno"));
		assertSame("Denis Dalton", a.navigateAndGetName("Bruno/Carla Costa/Dedé"));
		
		a.giveNicknameTo(b, "Bruninho");
		assertSame("Bruno Barros", a.navigateAndGetName("Bruninho"));
	}

}
