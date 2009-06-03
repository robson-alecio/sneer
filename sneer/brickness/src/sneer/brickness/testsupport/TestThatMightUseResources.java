package sneer.brickness.testsupport;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;

import sneer.commons.threads.Daemon;

public abstract class TestThatMightUseResources extends AssertUtils {

	private File _tmpDirectory;
	private Set<Thread> _activeThreadsBeforeTest;

	protected File tmpDirectory() {
		if (_tmpDirectory == null)
			_tmpDirectory = createTmpDirectory();

		return _tmpDirectory;
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

			if (thread.getState() == Thread.State.TERMINATED) continue;

			final LeakingThreadStopped plug = new LeakingThreadStopped(thread, "This thread was leaked by test: " + this.getClass() + " and it's now being stopped!");
			plug.printStackTrace();
			thread.stop(plug);
		}
	}

	@After
	public void afterTestThatMightUseResources() {
		Daemon.killAllInstances(); //Fix: This might be killing Daemons created before the test started.
		checkThreadLeak();
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
}
