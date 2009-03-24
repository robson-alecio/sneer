package sneer.commons.environments.tests;

import static sneer.commons.environments.Environments.my;

import org.junit.Assert;
import org.junit.Test;

import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.commons.lang.Producer;


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
		Environments.runWith(
			new Environment() { @Override public <T> T provide(Class<T> intrface) {
				return null;
			}},
			new Runnable() { @Override public void run() {
				my(Runnable.class);
			}}
		);
	}
	
	@Test
	public void testRunWith() {
		Environments.runWith(environment(), runnable());
		assertTrue(_ran);
	}
	
	@Test
	public void testBind() {
		
		final Producer<Object> producer = new Producer<Object>() { @Override public Object produce() {
			_ran = true;
			return my(Object.class);
		}};
		
		final Producer<Object> proxy = Environments.wrap(Producer.class, environment(producer));
		assertSame(_binding, proxy.produce());
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
