package sneer.pulp.threadpool.tests;

import static org.junit.Assert.assertSame;

import org.junit.Test;

import sneer.kernel.container.ContainerUtils;
import sneer.pulp.threadpool.ThreadPool;
import wheel.lang.Environment;

public class ThreadPoolTest {

	final ThreadPool subject = ContainerUtils.newContainer().provide(ThreadPool.class);
	
	@Test
	public void testEnvironmentIsPropagated() throws Exception {
		final Environment.Provider provider1 = new Environment.Provider() { @Override public <T> T provide(Class<T> intrface) {
			return null;
		}};
		
		final java.util.concurrent.Exchanger<Environment.Provider> exchanger = new java.util.concurrent.Exchanger<Environment.Provider>();
		
		Environment.runWith(provider1, new Runnable() { @Override public void run() {
			subject.registerActor(new Runnable() { @Override public void run() {
				try {
					assertSame(provider1, exchanger.exchange(Environment.current()));
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				}
			}});
		}});
		
		assertSame(provider1, exchanger.exchange(provider1));
	}

}
