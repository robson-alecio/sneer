package sneer.bricks.hardwaresharing.files.tests;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;

import sneer.bricks.hardwaresharing.files.writer.FileWriter;
import sneer.bricks.pulp.crypto.Sneer1024;

public class RemoteCopyTest extends FileCopyTest {

	private final FileWriter _writer = my(FileWriter.class);

	
	@Override
	protected void copyFromFileCache(Sneer1024 hashOfContents, File destination) throws IOException {
		_writer.writeTo(destination, anyReasonableDate(), hashOfContents);
	}

	
	private long anyReasonableDate() {
		return System.currentTimeMillis();
	}
}
