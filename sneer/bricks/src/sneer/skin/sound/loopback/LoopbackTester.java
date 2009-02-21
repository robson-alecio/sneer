package sneer.skin.sound.loopback;

import sneer.kernel.container.Brick;

public interface LoopbackTester extends Brick {

	boolean start();
	void stop();
}