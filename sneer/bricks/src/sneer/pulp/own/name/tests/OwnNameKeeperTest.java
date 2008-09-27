package sneer.pulp.own.name.tests;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.own.name.OwnNameKeeper;

public class OwnNameKeeperTest extends TestThatIsInjected {

	@Inject
	private static OwnNameKeeper _nameKeeper;
	
	@Test
	public void test() throws Exception {

		_nameKeeper.nameSetter().consume("Sandro Bihaiko");
		assertEquals("Sandro Bihaiko", _nameKeeper.name().currentValue());
		
		_nameKeeper.nameSetter().consume("Nell Daux");
		assertEquals("Nell Daux", _nameKeeper.name().currentValue());
	}
}
