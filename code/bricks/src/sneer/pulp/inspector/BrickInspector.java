package sneer.pulp.inspector;

import java.io.File;

public interface BrickInspector {

	BrickInfo loadBrickInfo(File brickDirectory);

}
