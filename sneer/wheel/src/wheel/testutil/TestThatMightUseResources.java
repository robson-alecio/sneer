package wheel.testutil;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.runner.RunWith;

import wheel.io.Logger;
import wheel.lang.Daemon;

@RunWith(WheelEnvironment.class)
public abstract class TestThatMightUseResources extends Assert {

	private File _tmpDirectory;

	protected File tmpDirectory() {
		if (_tmpDirectory == null)
			_tmpDirectory = createTmpDirectory();

		return _tmpDirectory;
	}

	@After
	public void afterTestThatMightUseResources() {
		Daemon.killAllInstances();
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
				FileUtils.deleteDirectory(tmp);
				return;
			} catch (IOException e) {
				if (System.currentTimeMillis() - t0 > 1000) {
					Logger.log("Test left files open: {}", e.getMessage());
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
