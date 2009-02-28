package sneer.brickness.testsupport.tests;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static sneer.brickness.environments.Environments.my;

import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.brickness.testsupport.BricknessTestEnvironment;
import sneer.brickness.testsupport.Contribute;
import sneer.brickness.testsupport.tests.bar.BarBrick;
import sneer.brickness.testsupport.tests.foo.FooBrick;

@RunWith(BricknessTestEnvironment.class)
public class BricknessTestEnvironmentTest {
	
	{
		my(BricknessTestEnvironment.class).instanceBeingInitialized(this);
	}
	
	@Contribute
	final BarBrick _bar = new BarBrick() {
	};
	
	final FooBrick _foo = new FooBrick() {
		@Override
		public BarBrick bar() {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
		}
	};
	
	// DONT TRY TO USE my IN FIELD INITIALIZERS:
	//		final ThreadPool _subject = my(ThreadPool.class);
	// Because a full blown container is not available during
	// the test instantiation.
	// Use the TestThatIsInjected base class and @Inject
	// annotation.
	
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
