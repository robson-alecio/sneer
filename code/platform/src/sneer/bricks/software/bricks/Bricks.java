package sneer.bricks.software.bricks;

import java.io.File;
import java.io.IOException;

import sneer.foundation.brickness.Brick;

@Brick
public interface Bricks {
	
	void install(File sourceDirectory) throws IOException;
	
}
