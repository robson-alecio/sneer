package functionaltests;

import static org.junit.Assert.assertSame;

import org.junit.Test;

import sneer.lego.Brick;

public class Freedom2Test {

	@Brick
	private SovereignCommunity _subject;
	
	@Test
	public void testNicknames() {
		SovereignParty a = _subject.createParty("Ana Almeida");
		SovereignParty b = _subject.createParty("Bruno Barros");
		SovereignParty c = _subject.createParty("Carla Costa");
		SovereignParty d = _subject.createParty("Denis Dalton");
		
		a.connectTo(b);
		a.connectTo(c);
		b.connectTo(c);
		c.connectTo(d);
		
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
