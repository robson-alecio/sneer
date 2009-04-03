package sneer.container;

import java.io.File;
import java.io.IOException;

import sneer.commons.environments.Environment;

public interface Brickness {

	void runBrick(File interfaceFile) throws IOException, BrickLoadingException;

	Environment environment();

}
