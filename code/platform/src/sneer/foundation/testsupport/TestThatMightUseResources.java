package sneer.foundation.testsupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import org.junit.After;
import org.junit.Before;



public abstract class TestThatMightUseResources extends AssertUtils {

	private File _tmpFolder;
	private Set<Thread> _activeThreadsBeforeTest;

	protected File tmpFolder() {
		if (_tmpFolder == null)
			_tmpFolder = createTmpFolder();

		return _tmpFolder;
	}

	@Before
	public void beforeTestThatMightUseResources() {
		_activeThreadsBeforeTest = Thread.getAllStackTraces().keySet();
	}

	@SuppressWarnings("deprecation")
	private void checkThreadLeak() {
		Set<Thread> activeThreadsAfterTest = Thread.getAllStackTraces().keySet();

		for (Thread thread : activeThreadsAfterTest) {
			if(_activeThreadsBeforeTest.contains(thread)) continue;

			if (waitForTermination(thread)) continue;

			final LeakingThreadStopped plug = new LeakingThreadStopped(thread, "This thread was leaked by test: " + this.getClass() + " and it's now being stopped!");
			plug.printStackTrace();
			thread.stop(plug);
		}
	}

	private boolean waitForTermination(Thread thread) {
		long t0 = System.currentTimeMillis();
		while (true) {
			if (thread.getState() == Thread.State.TERMINATED) return true;
			if (System.currentTimeMillis() - t0 > 200) return false;
			sleep(10);
		}
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	@After
	public void afterTestThatMightUseResources() {
		checkThreadLeak();
		deleteFiles();
	}

	private void deleteFiles() {
		if (_tmpFolder == null) return;
		try {
			tryToClean(_tmpFolder);
		} finally {
			_tmpFolder = null;
		}
	}
	
	private void tryToClean(File tmp) {
		long t0 = System.currentTimeMillis();
		while (true) {
			try {
				deleteFolder(tmp);
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
	
	private File createTmpFolder() {
		File result = new File(System.getProperty("java.io.tmpdir"), "" + System.nanoTime());
		assertTrue(result.mkdirs());
		return result;
	}

	class LeakingThreadStopped extends Throwable {

		public LeakingThreadStopped(Thread leakingThread, String message) {
			super(message);
			setStackTrace(leakingThread.getStackTrace());
		}

		@Override
		public synchronized Throwable fillInStackTrace() {
			return this;
		}
	}

	void deleteFolder(File folder) throws IOException {
		if (!folder.exists()) return;
		if (!folder.isDirectory()) 
			throw new IllegalArgumentException(folder.getAbsolutePath() + " is not a folder");

		recursiveDelete(folder);

		if (!folder.delete()) 
			throw new IOException("Unable to delete folder: " + folder.getAbsolutePath());
	}

	private void recursiveDelete(File folder) throws IOException, FileNotFoundException {
		for (File file : folder.listFiles()) {
			if (!file.exists()) 
				throw new FileNotFoundException("File does not exist: " + file.getAbsolutePath());
			
			if (file.isFile() && !file.delete()) 
				throw new IOException(("Unable to delete file: " + file.getAbsolutePath()));
			
			deleteFolder(file);
		}
	}

}

