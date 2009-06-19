package sneer.bricks.software.code.filefilters.java;

import java.io.File;

import sneer.foundation.brickness.Brick;

@Brick
public interface JavaFilters {

	JavaFilter newInstance(File root);

}
