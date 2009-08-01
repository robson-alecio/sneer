package sneer.foundation.testsupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;


@RunWith(TestThatMightUseResourcesRunner.class)
public abstract class TestThatMightUseResources extends AssertUtils {

	private File _tmpFolder;
	
	private Set<Thread> _activeThreadsBeforeTest;

	private final PrintStreamSentinel _outSentinel = new PrintStreamSentinel(System.out);
	private final PrintStreamSentinel _errSentinel = new PrintStreamSentinel(System.err);

	
	protected File tmpFolder() {
		if (_tmpFolder == null)
			_tmpFolder = createTmpFolder();

		return _tmpFolder;
	}

	
	@Before
	public void beforeTestThatMightUseResources() {
		_activeThreadsBeforeTest = Thread.getAllStackTraces().keySet();
		
		System.setOut(_outSentinel);
		System.setErr(_errSentinel);
	}

	
	@SuppressWarnings("deprecation")
	private void checkThreadLeak() {
		Set<Thread> activeThreadsAfterTest = Thread.getAllStackTraces().keySet();

		for (Thread thread : activeThreadsAfterTest) {
			if(_activeThreadsBeforeTest.contains(thread)) continue;

			if (waitForTermination(thread)) continue;

			final LeakingThreadStopped plug = new LeakingThreadStopped(thread, "" + thread + " was leaked by test: " + this.getClass() + " and is now being stopped!");
			thread.stop(plug);
			
			throw new IllegalStateException(plug);
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
		System.setOut(_outSentinel._delegate);
		System.setErr(_errSentinel._delegate);

		checkThreadLeak();
		deleteFiles();
	}


	private void checkConsolePollution() {
		_outSentinel.complainIfUsed();
		_errSentinel.complainIfUsed();
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
					throw new IllegalStateException(e);
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

	protected void afterSuccessfulTest() {
		checkConsolePollution();
	}

}

class PrintStreamSentinel extends PrintStream {
	
	IllegalStateException _exception;
	final PrintStream _delegate;
	
	PrintStreamSentinel(PrintStream delegate) {
		super(delegate);
		_delegate = delegate;
	}
	
	@Override public void write(byte[] buf, int off, int len) {
		try {
			throw new IllegalStateException("Test has passed but is writing to the console.");
		} catch (IllegalStateException e) {
			_exception = e;
		}
		super.write(buf, off, len);
	}
	
	void complainIfUsed() {
		if (_exception != null)	throw _exception;
	}
	
};
