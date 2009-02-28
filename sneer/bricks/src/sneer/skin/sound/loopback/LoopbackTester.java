package sneer.skin.sound.loopback;

import sneer.brickness.Brick;

public interface LoopbackTester extends Brick {

	boolean start();
	void stop();
}