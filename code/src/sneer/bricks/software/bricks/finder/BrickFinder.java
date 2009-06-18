package sneer.bricks.software.bricks.finder;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import sneer.foundation.brickness.Brick;

@Brick
public interface BrickFinder {

	Collection<String> findBricks(File binDirectory) throws IOException;

}
