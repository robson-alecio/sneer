package sneer.foundation.brickness.testsupport.tests;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static sneer.foundation.environments.Environments.my;

import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.foundation.brickness.testsupport.Bind;
import sneer.foundation.brickness.testsupport.BrickTestRunner;
import sneer.foundation.brickness.testsupport.tests.bar.BarBrick;
import sneer.foundation.brickness.testsupport.tests.foo.FooBrick;

@RunWith(BrickTestRunner.class)
public class BrickTestRunnerTest {
	
	{
		my(BrickTestRunner.class).instanceBeingInitialized(this);
	}
	
	@Bind
	final BarBrick _bar = new BarBrick() {};
	
	final FooBrick _foo = new FooBrick() {
		@Override
		public BarBrick bar() {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
		}
	};
	
	@Test
	public void testContributedField() {
		assertSame(_bar, my(BarBrick.class));
	}
	
	@Test
	public void testNonContributedField() {
		assertNotSame(_foo, my(FooBrick.class));
	}
	
	@Test
	public void testBrickSeesTestEnvironment() {
		assertSame(_bar, my(FooBrick.class).bar());
	}

}
