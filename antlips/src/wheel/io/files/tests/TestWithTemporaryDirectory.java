package wheel.io.files.tests;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;

import antlips.antFileGenerator.AntUtils;

public abstract class TestWithTemporaryDirectory extends Assert {

	private File _tmpDirectory;

	protected File tmpDirectory() {
		if (_tmpDirectory == null)
			_tmpDirectory = createTmpDirectory();

		return _tmpDirectory;
	}

	@After
	public void afterTestWithTmpDirectory() {
		deleteFiles();
	}

	private void deleteFiles() {
		if (_tmpDirectory == null) return;
		try {
			tryToClean(_tmpDirectory);
		} finally {
			_tmpDirectory = null;
		}
	}
	
	private void tryToClean(File tmp) {
		long t0 = System.currentTimeMillis();
		while (true) {
			try {
				AntUtils.deleteDirectory(tmp);
				return;
			} catch (IOException e) {
				if (System.currentTimeMillis() - t0 > 1000) {
					e.printStackTrace();
					return;
				}
				System.gc();
			}
		}
	}
	
	private File createTmpDirectory() {
		File result = new File(System.getProperty("java.io.tmpdir"), "" + System.nanoTime());
		assertTrue(result.mkdirs());
		return result;
	}
	

}
