package sneer.bricks.hardwaresharing.files;

import java.io.File;
import java.io.IOException;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.Brick;

@Brick
public interface FileSpace {

	Sneer1024 publishContents(File fileOrDirectory) throws IOException;

	void fetchContentsInto(File destination, Sneer1024 hash) throws IOException;

}
