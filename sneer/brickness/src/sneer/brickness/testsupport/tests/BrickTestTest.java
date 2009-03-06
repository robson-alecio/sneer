package sneer.brickness.testsupport.tests;

import static sneer.brickness.environments.Environments.my;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.brickness.testsupport.Contribute;
import sneer.brickness.testsupport.tests.bar.BarBrick;
import sneer.brickness.testsupport.tests.foo.FooBrick;

public class BrickTestTest extends BrickTest {
	
	@Contribute final BarBrick _bar = new BarBrick() {};
	
	final FooBrick _foo = my(FooBrick.class);
	
	@Test
	public void test() {
		assertSame(_bar, _foo.bar());
	}

}
