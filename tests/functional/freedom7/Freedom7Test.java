package functional.freedom7;

import java.io.File;

import org.junit.Test;

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
		//TODO: estimular signal tree local
		
		//already connected: receiver.bidirectionalConnectTo(publisher);
		receiver.meToo(publisher, BRICK_NAME);
		Object sample = receiver.produce(BRICK_NAME);
		System.out.println(sample);
		System.out.println(sample.getClass());
		System.out.println(sample.getClass().getClassLoader());
		//TODO: estimular signal tree local
	}

	protected abstract  File askSourceFolder();

	protected abstract BrickPublisher wrapParty(SovereignParty party);
}