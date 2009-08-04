package sneer.bricks.hardwaresharing.files.writer;

import java.io.File;
import java.io.IOException;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.Brick;

@Brick
public interface FileWriter {

	/** Retrieves the contents from the FileCache given hashOfContents and writes them to fileOrFolder. */
	void writeTo(File fileOrFolder, long lastModified, Sneer1024 hashOfContents) throws IOException;

}
