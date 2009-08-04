package sneer.bricks.hardwaresharing.files.tests;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;

import sneer.bricks.hardwaresharing.files.writer.FileWriter;
import sneer.bricks.pulp.crypto.Sneer1024;

public class LocalCopyTest extends FileCopyTest {

	private final FileWriter _writer = my(FileWriter.class);

	
	@Override
	protected File copyFromFileCache(File fileOrFolder, Sneer1024 hash) throws IOException {
		File result = newTempFile();
		_writer.writeTo(result, anyReasonableDate(), hash);
		return result;
	}

	
	private File newTempFile() {
		return new File(tmpFolder(), "destination" + System.nanoTime());
	}

	private long anyReasonableDate() {
		return System.currentTimeMillis();
	}
}
