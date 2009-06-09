package sneer.foundation.brickness.testsupport.tests;

import static sneer.foundation.commons.environments.Environments.my;

import org.junit.Test;

import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.brickness.testsupport.Contribute;
import sneer.foundation.brickness.testsupport.tests.bar.BarBrick;
import sneer.foundation.brickness.testsupport.tests.foo.FooBrick;

public class BrickTestTest extends BrickTest {
	
	@Contribute final BarBrick _bar = new BarBrick() {};
	
	final FooBrick _foo = my(FooBrick.class);
	
	@Test
	public void test() {
		BarBrick other = _foo.bar();
		assertSame(_bar, other);
	}

}
