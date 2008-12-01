package wheel.lang.tests;

import org.junit.Assert;
import org.junit.Test;

import wheel.lang.ByRef;
import wheel.lang.Environment;

public class EnvironmentTest extends Assert {
	
	@Test
	public void testRunWithMy() {
		final Object binding = new Object();
		final ByRef<Boolean> ran = ByRef.newInstance(false);
		
		Environment.runWith(new Environment.Provider() { @Override public <T> T provide(Class<T> intrface) {
			return (T) binding;
		}},
		new Runnable() { @Override public void run() {
			assertEquals(binding, Environment.my(Object.class));
			ran.value = true;
		}});
		assertTrue(ran.value);
	}

	@Test
	public void testCurrent() {
		
		final Environment.Provider provider = new Environment.Provider() { @Override public <T> T provide(Class<T> intrface) {
			return null;
		}};
		Environment.runWith(provider, new Runnable() { @Override public void run() {
			assertSame(provider, Environment.current());
		}});
	
	}
}
