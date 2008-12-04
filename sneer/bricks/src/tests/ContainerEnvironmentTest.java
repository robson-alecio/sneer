package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static wheel.lang.Environments.my;

import org.junit.Test;
import org.junit.runner.RunWith;

import tests.bar.BarBrick;
import tests.foo.FooBrick;

@RunWith(ContainerEnvironment.class)
public class ContainerEnvironmentTest {
	
	int _barPingCount = 0;
	// mocks can be initialized directly
	// and will be made part of the test environment
	// automatically (see #testFieldsAreAutomaticallyInjectedIntoTheEnvironment)
	final BarBrick _bar = new BarBrick() {
		@Override
		public void ping() {
			++_barPingCount;
		}
	};
	
	// DONT TRY TO USE my IN FIELD INITIALIZERS:
	//		final ThreadPool _subject = my(ThreadPool.class);
	// Because a full blown container is not available during
	// the test instantiation.
	// Use the TestThatIsInjected base class and @Inject
	// annotation.
	
	@Test
	public void testFieldsAreAutomaticallyInjectedIntoTheEnvironment() {
		assertSame(_bar, my(BarBrick.class));
	}
	
	@Test
	public void testBrickSeesTestEnvironment() {
		assertEquals(0, _barPingCount);
		my(FooBrick.class).pingBar();
		assertEquals(1, _barPingCount);
	}

}
