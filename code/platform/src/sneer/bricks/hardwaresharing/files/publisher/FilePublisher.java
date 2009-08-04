package sneer.bricks.hardwaresharing.files.publisher;

import java.io.File;
import java.io.IOException;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.Brick;

@Brick
public interface FilePublisher {

	/** Puts the contents of fileOrFolder in the FileCache.
	 * @return handle to be used to retrieve the published contents of fileOrFolder from the cache. */
	Sneer1024 publish(File fileOrFolder) throws IOException;

}
