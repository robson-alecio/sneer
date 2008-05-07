package functional.freedom7;

import java.io.File;

import org.junit.Test;

import functional.SovereignFunctionalTest;
import functional.SovereignParty;
import functional.TestDashboard;

public abstract class Freedom7Test extends SovereignFunctionalTest {

	private static final String INTERFACE = "sneer.bricks.sample.Sample";
	//private static final String IMPL = "sneer.bricks.sample.impl.SampleImpl";
	
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
		receiver.meToo(INTERFACE);
		//TODO: estimular signal tree local
	}

	protected abstract  File askSourceFolder();

	protected abstract BrickPublisher wrapParty(SovereignParty party);
}