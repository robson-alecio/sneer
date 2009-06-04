package sneer.commons.environments.tests;

import static sneer.commons.environments.Environments.my;

import org.junit.Assert;
import org.junit.Test;

import sneer.commons.environments.Environment;
import sneer.commons.environments.EnvironmentUtils;
import sneer.commons.environments.Environments;


public class EnvironmentsTest extends Assert {
	
	final Object _binding = new Object();
	boolean _ran = false;
	
	@Test
	public void testMyEnvironment() {
		final Environment environment = environment();
		Environments.runWith(environment, new Runnable() { @Override public void run() {
			assertSame(environment, my(Environment.class));
		}});
	}
	
	@Test(expected=IllegalStateException.class)
	public void testMyInUnsuitableEnvironment() {
		EnvironmentUtils.retrieveFrom(
			new Environment() { @Override public <T> T provide(Class<T> intrface) {
				return null;
			}}, Runnable.class);
	}
	
	@Test
	public void testRunWith() {
		Environments.runWith(environment(), runnable());
		assertTrue(_ran);
	}
	
	
	private Runnable runnable() {
		return new Runnable() { @Override public void run() {
			assertEquals(_binding, Environments.my(Object.class));
			_ran = true;
		}};
	}

	private Environment environment(final Object... bindings) {
		return new Environment() { @Override public <T> T provide(Class<T> intrface) {
			if (Object.class == intrface)
				return intrface.cast(_binding);
			
			for (Object binding : bindings)
				if (intrface.isInstance(binding))
					return intrface.cast(binding);
			
			throw new IllegalArgumentException();
		}};
	}

}
