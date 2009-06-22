package sneer.bricks.software.adapters.classfiles;

import java.io.File;

import sneer.foundation.brickness.Brick;

@Brick
public interface ClassFiles {

	File classpathRootFor(Class<?> brick);

}
