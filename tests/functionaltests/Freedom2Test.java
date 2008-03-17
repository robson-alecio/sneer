package functionaltests;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public abstract class Freedom2Test extends SovereignFunctionalTest {

	
	@Test
	public void testNicknames() {
		
		//if (!TestDashboard.newTestsShouldRun()) return;
		
		SovereignParty a = _community.createParty("127.0.0.1", 9090, "Ana Almeida");
		SovereignParty b = _community.createParty("127.0.0.1", 9091, "Bruno Barros");
		SovereignParty c = _community.createParty("127.0.0.1", 9092, "Carla Costa");
		SovereignParty d = _community.createParty("127.0.0.1", 9093, "Denis Dalton");
		
		a.bidirectionalConnectTo(b);
		a.bidirectionalConnectTo(c);
		b.bidirectionalConnectTo(c);
		c.bidirectionalConnectTo(d);
		
		a.giveNicknameTo(b, "Bruno");
		b.giveNicknameTo(a, "Aninha");
		a.giveNicknameTo(c, "Carla");
		c.giveNicknameTo(a, "Miguxa");
		c.giveNicknameTo(d, "Dedé");
		
		assertSame(b, a.navigateTo("Bruno"));
		assertSame(a, b.navigateTo("Carla Costa", "Miguxa"));
		assertSame(a, a.navigateTo("Bruno", "Aninha"));
		assertSame(b, a.navigateTo("Bruno", "Aninha", "Bruno"));
		assertSame(d, a.navigateTo("Bruno", "Carla Costa", "Dedé"));
		
		a.giveNicknameTo(b, "Bruninho");
		assertSame(b, a.navigateTo("Bruninho"));
	}

}
