package functional.freedom7;

import java.io.File;

import org.junit.Test;

import functional.SovereignFunctionalTest;
import functional.SovereignParty;

public abstract class Freedom7Test extends SovereignFunctionalTest {

	private static final String INTERFACE = "sneer.bricks.sample.Sample";
	private static final String IMPL = "sneer.bricks.sample.impl.SampleImpl";
	
	@Test
	public void testPublish() {
		
		BrickPublisher publisher = wrapParty(_a);
		BrickPublisher receiver = wrapParty(_b);

		File brickFile = askBrickFile();
		BrickPublished brick = publisher.publishBrick(brickFile);
		//estimular signal tree local
		
		receiver.bidirectionalConnectTo(publisher);
		receiver.meToo(INTERFACE);
		//estimular signal tree local
	}

	protected abstract  File askBrickFile();

	protected abstract BrickPublisher wrapParty(SovereignParty party);
}