package sneer.brickness;

import java.io.File;

import sneer.commons.environments.Environment;

public interface Brickness {

	void placeBrick(File classRootDirectory, String brickName);

	Environment environment();

}