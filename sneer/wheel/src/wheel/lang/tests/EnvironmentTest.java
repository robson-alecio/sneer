package wheel.lang.tests;

import org.junit.Assert;
import org.junit.Test;

import wheel.lang.ByRef;
import wheel.lang.Environment;
import wheel.lang.Environment.Memento;
import wheel.lang.Environment.Provider;

public class EnvironmentTest extends Assert {
	
	final Object _binding = new Object();
	final ByRef<Boolean> _ran = ByRef.newInstance(false);
	
	@Test
	public void testRunWithProvider() {
		Environment.runWith(provider(),	runnable());
		assertTrue(_ran.value);
	}

	@Test
	public void testRunWithMemento() {
		final ByRef<Memento> memento = ByRef.newInstance(); 
		
		Environment.runWith(provider(),	new Runnable(){ @Override public void run() {
			memento.value = Environment.memento();
		}});
		
		Environment.runWith(memento.value,	runnable());
		assertTrue(_ran.value);
	}

	
	private Runnable runnable() {
		return new Runnable() { @Override public void run() {
			assertEquals(_binding, Environment.my(Object.class));
			_ran.value = true;
		}};
	}

	private Provider provider() {
		return new Environment.Provider() { @Override public <T> T provide(Class<T> intrface) {
			return (T) _binding;
		}};
	}

}
