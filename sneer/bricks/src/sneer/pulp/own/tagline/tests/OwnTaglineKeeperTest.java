package sneer.pulp.own.tagline.tests;

import org.junit.Test;

import sneer.brickness.testsupport.TestInBricknessEnvironment;
import sneer.pulp.own.tagline.OwnTaglineKeeper;
import static sneer.brickness.Environments.my;

public class OwnTaglineKeeperTest extends TestInBricknessEnvironment {

	private final OwnTaglineKeeper _taglineKeeper = my(OwnTaglineKeeper.class);
	
	@Test
	public void test() throws Exception {

		_taglineKeeper.taglineSetter().consume("Zubs1");
		assertEquals("Zubs1", _taglineKeeper.tagline().currentValue());
		
		_taglineKeeper.taglineSetter().consume("Zubs2");
		assertEquals("Zubs2", _taglineKeeper.tagline().currentValue());
		
	}
}
