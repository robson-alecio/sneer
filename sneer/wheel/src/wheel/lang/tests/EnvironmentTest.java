package wheel.lang.tests;

import org.junit.Assert;
import org.junit.Test;

import wheel.lang.ByRef;
import wheel.lang.Environments;
import wheel.lang.Environment;
import wheel.lang.Environments.Memento;

public class EnvironmentTest extends Assert {
	
	final Object _binding = new Object();
	final ByRef<Boolean> _ran = ByRef.newInstance(false);
	
	@Test
	public void testRunWithProvider() {
		Environments.runWith(environment(), runnable());
		assertTrue(_ran.value);
	}

	@Test
	public void testRunWithMemento() {
		final ByRef<Memento> memento = ByRef.newInstance(); 
		
		Environments.runWith(environment(), new Runnable(){ @Override public void run() {
			memento.value = Environments.memento();
		}});
		
		Environments.runWith(memento.value,	runnable());
		assertTrue(_ran.value);
	}

	
	private Runnable runnable() {
		return new Runnable() { @Override public void run() {
			assertEquals(_binding, Environments.my(Object.class));
			_ran.value = true;
		}};
	}

	private Environment environment() {
		return new Environment() { @Override public <T> T provide(Class<T> intrface) {
			return (T) _binding;
		}};
	}

}
