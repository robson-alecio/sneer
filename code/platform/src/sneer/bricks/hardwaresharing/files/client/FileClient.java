package sneer.bricks.hardwaresharing.files.client;

import java.io.File;
import java.io.IOException;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.Brick;

@Brick
public interface FileClient {

	void fetchContentsInto(File fileOrFolder, long lastModified, Sneer1024 hash) throws IOException;

}
