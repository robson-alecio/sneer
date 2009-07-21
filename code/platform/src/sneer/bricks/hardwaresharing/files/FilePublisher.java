package sneer.bricks.hardwaresharing.files;

import java.io.File;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.Brick;

@Brick
public interface FilePublisher {

	Sneer1024 publishDirectory(File directory);

}
