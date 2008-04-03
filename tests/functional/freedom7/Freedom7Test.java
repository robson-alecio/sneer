package functional.freedom7;

import static org.junit.Assert.*;

import org.junit.Test;

public abstract class Freedom7Test {

	private static final String INTERFACE = "sneer.bricks.sample.Sample";
	private static final String IMPL = "sneer.bricks.sample.impl.SampleImpl";
	
	@Test
	public void testPublish() {
		Peer peer = createPeer();
		BrickPublished brick = peer.publishBrick(INTERFACE);
		peer.deploy(brick);
		Object component = peer.lookup(INTERFACE);
		assertEquals(IMPL, component.getClass().getName());
	}

	protected abstract Peer createPeer();
}