package sneer.pulp.threadpool.tests;

import static org.junit.Assert.assertSame;

import org.junit.Test;

import sneer.kernel.container.ContainerUtils;
import sneer.pulp.threadpool.ThreadPool;
import wheel.lang.Environments;
import wheel.lang.Environment;
import wheel.lang.Threads;

public class ThreadPoolTest {

	final ThreadPool subject = ContainerUtils.newContainer().provide(ThreadPool.class);

	final Object binding = new Object();

	final Object ranMonitor = new Object();
	volatile boolean ran = false;
	
	@Test (timeout = 2000)
	public void testEnvironmentIsPropagated() throws Exception {
		
		
		final Environment environment = new Environment() { @Override public <T> T provide(Class<T> intrface) {
			return (T) binding;
		}};
		
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
