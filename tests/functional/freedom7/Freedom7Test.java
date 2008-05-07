package functional.freedom7;

import java.io.File;

import org.junit.Test;
import static org.junit.Assert.*;

import functional.SovereignFunctionalTest;
import functional.SovereignParty;
import functional.TestDashboard;

public abstract class Freedom7Test extends SovereignFunctionalTest {

	private static final String BRICK_NAME = "sneer.bricks.sample.Sample";
	
	@Test
	public void testPublish() {
		
		if (!TestDashboard.newTestsShouldRun()) return;
		
		BrickPublisher publisher = wrapParty(_a);
		BrickPublisher receiver = wrapParty(_b);

		File sourceFolder = askSourceFolder();
		/*
		 * compiles all bricks found under _sourceFolder_ and installs them locally
		 */
		publisher.publishBrick(sourceFolder);
		Object orig = publisher.produce(BRICK_NAME);
		print("sneer+AnaAlmeida",orig);
		
		receiver.meToo(publisher, BRICK_NAME);
		Object copied = receiver.produce(BRICK_NAME);
		print("sneer+BrunoBarros",copied);
	}

	private void print(String expected, Object obj) {
		String toString = obj.getClass().getClassLoader().toString();
		//System.out.println(toString);
		assertTrue("wrong directory for brick class loader: "+toString,toString.indexOf(expected) > 0);
	}

	protected abstract  File askSourceFolder();

	protected abstract BrickPublisher wrapParty(SovereignParty party);
}