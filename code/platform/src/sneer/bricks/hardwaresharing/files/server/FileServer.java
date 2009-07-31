package sneer.bricks.hardwaresharing.files.server;

import java.io.File;
import java.io.IOException;

import sneer.bricks.hardwaresharing.files.client.FileClient;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.Brick;

@Brick
public interface FileServer {

	/**
	 * @return handle to be used to retrieve the published contents of fileOrFolder
	 * @see FileClient#fetchContentsInto(File, long, Sneer1024)*/
	Sneer1024 serve(File fileOrFolder) throws IOException;

}
