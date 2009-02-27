package wheel.lang.tests;

import static sneer.brickness.Environments.my;

import org.junit.Assert;
import org.junit.Test;

import sneer.brickness.ByRef;
import sneer.brickness.Environment;
import sneer.brickness.Environments;
import sneer.brickness.Environments.Memento;

import wheel.lang.Producer;

public class EnvironmentsTest extends Assert {
	
	final Object _binding = new Object();
	boolean _ran = false;
	
	@Test
	public void testRunWithProvider() {
		Environments.runWith(environment(), runnable());
		assertTrue(_ran);
	}
	
	@Test
	public void testBind() {
		
		final Producer<Object> producer = new Producer<Object>() { @Override public Object produce() {
			_ran = true;
			return my(Object.class);
		}};
		
		final Producer<Object> proxy = Environments.bind(environment(producer), Producer.class);
		assertSame(_binding, proxy.produce());
	}

	@Test
	public void testRunWithMemento() {
		final ByRef<Memento> memento = ByRef.newInstance(); 
		
		Environments.runWith(environment(), new Runnable(){ @Override public void run() {
			memento.value = Environments.memento();
		}});
		
		Environments.runWith(memento.value,	runnable());
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
