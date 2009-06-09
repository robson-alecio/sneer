package sneer.software.code.filefilters.java;

import java.io.File;

import sneer.brickness.Brick;

@Brick
public interface JavaFilters {

	JavaFilter newInstance(File root);

}
