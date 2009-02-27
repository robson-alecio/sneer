package sneer.brickness.testsupport.tests;

import org.junit.Test;

import sneer.brickness.testsupport.Contribute;
import sneer.brickness.testsupport.TestInContainerEnvironment;
import sneer.brickness.testsupport.tests.bar.BarBrick;
import sneer.brickness.testsupport.tests.foo.FooBrick;
import static sneer.brickness.Environments.my;

public class TestInContainerEnvironmentTest extends TestInContainerEnvironment {
	
	@Contribute final BarBrick _bar = new BarBrick() {};
	
	final FooBrick _foo = my(FooBrick.class);
	
	@Test
	public void test() {
		assertSame(_bar, _foo.bar());
	}

}
