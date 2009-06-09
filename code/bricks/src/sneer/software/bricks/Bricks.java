package sneer.software.bricks;

import java.io.File;

import sneer.brickness.Brick;

@Brick
public interface Bricks {
	void publish(File sourceDirectory);
}
