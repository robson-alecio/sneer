package functional.freedom7;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import functional.SovereignFunctionalTest;
import functional.SovereignParty;
import functional.TestDashboard;

public abstract class Freedom7Test extends SovereignFunctionalTest {

	private static final String BRICK_NAME = "sneer.bricks.sample.Sample";
	
	@Test
	public void testPublish() throws Exception {
		
		if (!TestDashboard.newTestsShouldRun()) return;
		
		BrickPublisher publisher = wrapParty(_a);
		BrickPublisher receiver = wrapParty(_b);

		File sourceFolder = askSourceFolder();
		/*
		 * compiles all bricks found under _sourceFolder_ and installs them locally
		 */
		publisher.publishBrick(sourceFolder);

		Object brick1 = publisher.produce(BRICK_NAME);
		ClassLoader cl1 = brick1.getClass().getClassLoader();
		assertTrue("wrong directory for brick class loader: "+cl1.toString(),cl1.toString().indexOf("sneer+AnaAlmeida") > 0);
		
		receiver.meToo(publisher, BRICK_NAME);
		Object brick2 = receiver.produce(BRICK_NAME);
		ClassLoader cl2 = brick2.getClass().getClassLoader();
		assertTrue("wrong directory for brick class loader: "+cl2.toString(),cl2.toString().indexOf("sneer+BrunoBarros") > 0);
		
		assertNotSame(brick1, brick2);
		assertNotSame(cl1, cl2);
	}

	protected abstract  File askSourceFolder();

	protected abstract BrickPublisher wrapParty(SovereignParty party);
}