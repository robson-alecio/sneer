package spikes.sneer.bricks.skin.audio.loopback;

import sneer.foundation.brickness.Brick;

@Brick
public interface LoopbackTester {

	boolean start();
	void stop();
}