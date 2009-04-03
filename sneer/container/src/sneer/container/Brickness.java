package sneer.container;

import java.io.File;

import sneer.commons.environments.Environment;

public interface Brickness {

	void runBrick(File classRootDirectory, String brickName) throws BrickLoadingException;

	Environment environment();

}
