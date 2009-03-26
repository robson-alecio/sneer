package sneer.container;

import java.io.IOException;

public interface NewContainer {

	void runBrick(String brickDirectory) throws IOException, BrickLoadingException;

}
