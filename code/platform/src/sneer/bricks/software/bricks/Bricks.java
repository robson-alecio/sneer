package sneer.bricks.software.bricks;

import java.io.File;

import sneer.foundation.brickness.Brick;

@Brick
public interface Bricks {
	void publish(File sourceDirectory);
}
