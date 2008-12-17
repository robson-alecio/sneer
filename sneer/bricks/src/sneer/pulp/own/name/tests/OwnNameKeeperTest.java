package sneer.pulp.own.name.tests;

import org.junit.Test;

import sneer.pulp.own.name.OwnNameKeeper;
import tests.TestThatIsInjected;
import static wheel.lang.Environments.my;

public class OwnNameKeeperTest extends TestThatIsInjected {

	private OwnNameKeeper _nameKeeper = my(OwnNameKeeper.class);
	
	@Test
	public void test() throws Exception {

		_nameKeeper.nameSetter().consume("Sandro Bihaiko");
		assertEquals("Sandro Bihaiko", _nameKeeper.name().currentValue());
		
		_nameKeeper.nameSetter().consume("Nell Daux");
		assertEquals("Nell Daux", _nameKeeper.name().currentValue());
	}
}
