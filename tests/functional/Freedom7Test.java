package functional;

import static org.junit.Assert.*;

import org.junit.Test;

public abstract class Freedom7Test {

	private static final String INTERFACE = "sneer.bricks.sample.Sample";
	private static final String IMPL = "sneer.bricks.sample.impl.SampleImpl";
	
	@Test
	public void testPublish() {
		Peer peer = new Peer();
		BrickPublished brick = peer.publishBrick(INTERFACE);
		peer.deploy(brick);
		Object component = peer.lookup(INTERFACE);
		assertEquals(IMPL, component.getClass().getName());
	}
}


class Peer {

	public BrickPublished publishBrick(String string) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	public Object lookup(String string) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	public void deploy(BrickPublished brick) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}
	
}

class BrickPublished {
	
}