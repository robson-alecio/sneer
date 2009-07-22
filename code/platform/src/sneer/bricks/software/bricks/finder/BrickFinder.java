
package sneer.bricks.software.bricks.finder;

import java.io.IOException;
import java.util.Collection;

import sneer.foundation.brickness.Brick;

@Brick
public interface BrickFinder {
	
	Collection<String> findBricks() throws IOException;

}
