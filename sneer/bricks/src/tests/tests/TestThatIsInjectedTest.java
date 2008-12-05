package tests.tests;

import org.junit.Test;

import tests.Contribute;
import tests.TestThatIsInjected;
import tests.tests.bar.BarBrick;
import tests.tests.foo.FooBrick;
import static wheel.lang.Environments.my;

public class TestThatIsInjectedTest extends TestThatIsInjected {
	
	@Contribute final BarBrick _bar = new BarBrick() {};
	
	final FooBrick _foo = my(FooBrick.class);
	
	@Test
	public void test() {
		assertSame(_bar, _foo.bar());
	}

}
