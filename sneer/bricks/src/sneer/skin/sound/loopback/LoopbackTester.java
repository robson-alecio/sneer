package sneer.skin.sound.loopback;

import sneer.brickness.Brick;

@Brick
public interface LoopbackTester {

	boolean start();
	void stop();
}