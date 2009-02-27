package tests.tests;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static sneer.brickness.Environments.my;

import org.junit.Test;
import org.junit.runner.RunWith;

import tests.ContainerEnvironment;
import tests.Contribute;
import tests.tests.bar.BarBrick;
import tests.tests.foo.FooBrick;

@RunWith(ContainerEnvironment.class)
public class ContainerEnvironmentTest {
	
	{
		my(ContainerEnvironment.class).instanceBeingInitialized(this);
	}
	
	@Contribute
	final BarBrick _bar = new BarBrick() {
	};
	
	final FooBrick _foo = new FooBrick() {
		@Override
		public BarBrick bar() {
			throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
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
