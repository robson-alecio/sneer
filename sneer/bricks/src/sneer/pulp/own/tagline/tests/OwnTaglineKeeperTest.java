package sneer.pulp.own.tagline.tests;

import static sneer.brickness.environments.Environments.my;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.pulp.own.tagline.OwnTaglineKeeper;

public class OwnTaglineKeeperTest extends BrickTest {

	private final OwnTaglineKeeper _taglineKeeper = my(OwnTaglineKeeper.class);
	
	@Test
	public void test() throws Exception {

		_taglineKeeper.taglineSetter().consume("Zubs1");
		assertEquals("Zubs1", _taglineKeeper.tagline().currentValue());
		
		_taglineKeeper.taglineSetter().consume("Zubs2");
		assertEquals("Zubs2", _taglineKeeper.tagline().currentValue());
		
	}
}
