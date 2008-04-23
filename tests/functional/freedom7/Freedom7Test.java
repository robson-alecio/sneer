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
		publisher.publishBrick(sourceFolder);
		//estimular signal tree local
		
		receiver.bidirectionalConnectTo(publisher);
		receiver.meToo(INTERFACE);
		//estimular signal tree local
	}

	protected abstract  File askSourceFolder();

	protected abstract BrickPublisher wrapParty(SovereignParty party);
}