package sneer.bricks.skin.sound.loopback;

import sneer.foundation.brickness.Brick;

@Brick
public interface LoopbackTester {

	boolean start();
	void stop();
}