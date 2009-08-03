package sneer.bricks.hardwaresharing.files;

import java.io.File;
import java.io.IOException;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.Brick;

@Brick
public interface FileSpace {

	Sneer1024 publishContents(File fileOrFolder) throws IOException;

	void fetchContentsInto(File destinationFileOrFolder, long lastModified, Sneer1024 hash) throws IOException;

}
