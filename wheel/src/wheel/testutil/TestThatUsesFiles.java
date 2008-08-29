package wheel.testutil;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;

public class TestThatUsesFiles {

	private File _tmpDirectory;

	protected File tmpDirectory() {
		if (_tmpDirectory == null) {
			File parent = new File(System.getProperty("java.io.tmpdir"));
			_tmpDirectory = new File(parent, "" + System.nanoTime());
			assertTrue(_tmpDirectory.mkdirs());
		}
		return _tmpDirectory;
	}
	
	@After
	public void cleanup() {
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
				FileUtils.deleteDirectory(tmp);
				return;
			} catch (IOException e) {
				if (System.currentTimeMillis() - t0 > 3000) {
					System.err.println("Test left files open: " + e.getMessage());
					return;
				}
				System.gc();
			}
		}
	}


}
