package sneer.pulp.own.tagline.tests;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.pulp.own.tagline.OwnTaglineKeeper;
import tests.TestThatIsInjected;

public class OwnTaglineKeeperTest extends TestThatIsInjected {

	@Inject
	private static OwnTaglineKeeper _taglineKeeper;
	
	@Test
	public void test() throws Exception {

		_taglineKeeper.taglineSetter().consume("Zubs1");
		assertEquals("Zubs1", _taglineKeeper.tagline().currentValue());
		
		_taglineKeeper.taglineSetter().consume("Zubs2");
		assertEquals("Zubs2", _taglineKeeper.tagline().currentValue());
		
	}
}
