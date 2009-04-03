package sneer.pulp.threadpool.tests;

import static org.junit.Assert.assertSame;
import static sneer.commons.environments.Environments.my;

import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.brickness.testsupport.BrickTestRunner;
import sneer.commons.environments.Bindings;
import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.pulp.threadpool.ThreadPool;
import wheel.lang.Threads;

@RunWith(BrickTestRunner.class)
public class ThreadPoolTest {

	final ThreadPool subject = my(ThreadPool.class);

	final Object binding = new Object();

	final Object ranMonitor = new Object();
	
	volatile boolean ran = false;
	
	@Test (timeout = 2000)
	public void testEnvironmentIsPropagated() throws Exception {
		final Environment testEnvironment = new Bindings(binding).environment();
		
		Environment environment = Environments.compose(testEnvironment, my(Environment.class));
		
		Environments.runWith(environment, new Runnable() { @Override public void run() {
			subject.registerActor(new Runnable() { @Override public void run() {
				assertSame(binding, Environments.my(Object.class));
				synchronized (ranMonitor) {
					ran = true;
					ranMonitor.notify();
				}
			}});
		}});
		
		synchronized (ranMonitor) {
			if (!ran)
				Threads.waitWithoutInterruptions(ranMonitor);
		}
	}

}
