package wheel.io.files.impl.tests;

import java.io.File;
import java.io.IOException;

import wheel.io.files.Directory;
import wheel.io.files.impl.DurableDirectory;
import wheel.io.files.tests.DirectoryTest;

public class DurableDirectoryTest extends DirectoryTest {

	@Override
	protected Directory subject() throws IOException {
		return new DurableDirectory(tmpDirectory().getAbsolutePath());
	}

	@Override
	protected String absoluteFileName(String filename) {
		return new File(tmpDirectory(), filename).getAbsolutePath();
	}

}