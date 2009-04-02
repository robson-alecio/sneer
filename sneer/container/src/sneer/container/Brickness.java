package sneer.container;

import java.io.IOException;

import sneer.commons.environments.Environment;

public interface Brickness {

	void runBrick(String brickDirectory) throws IOException, BrickLoadingException;

	Environment environment();

}
