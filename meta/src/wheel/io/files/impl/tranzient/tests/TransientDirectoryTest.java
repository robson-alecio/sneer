package wheel.io.files.impl.tranzient.tests;

import wheel.io.files.Directory;
import wheel.io.files.impl.tranzient.TransientDirectory;
import wheel.io.files.tests.DirectoryTestBase;

public class TransientDirectoryTest extends DirectoryTestBase {


	@Override
	protected Directory subject() {
		return new TransientDirectory();
	}

	@Override
	protected String absoluteFileName(String filename) {
		return filename;
	}

}
