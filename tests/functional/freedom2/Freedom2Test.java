package functional.freedom2;

import static org.junit.Assert.assertSame;

import org.junit.Test;

import functional.SovereignFunctionalTest;

public abstract class Freedom2Test extends SovereignFunctionalTest {

	
	@Test
	public void testNicknames() {
		
		_a.giveNicknameTo(_b, "Bruno");
		_b.giveNicknameTo(_a, "Aninha");
		_a.giveNicknameTo(_c, "Carla");
		_c.giveNicknameTo(_a, "Miguxa");
		_c.giveNicknameTo(_d, "Dedé");
		
		assertSame("Bruno Barros", _a.navigateAndGetName("Bruno"));
		assertSame("Ana Almeida", _b.navigateAndGetName("Carla Costa/Miguxa"));
		assertSame("Ana Almeida", _a.navigateAndGetName("Bruno/Aninha"));
		assertSame("Bruno Barros", _a.navigateAndGetName("Bruno/Aninha/Bruno"));
		assertSame("Denis Dalton", _a.navigateAndGetName("Bruno/Carla Costa/Dedé"));
		
		_a.giveNicknameTo(_b, "Bruninho");
		assertSame("Bruno Barros", _a.navigateAndGetName("Bruninho"));
	}

}
