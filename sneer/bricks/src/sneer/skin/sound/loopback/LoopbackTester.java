package sneer.skin.sound.loopback;

import sneer.brickness.OldBrick;

public interface LoopbackTester extends OldBrick {

	boolean start();
	void stop();
}