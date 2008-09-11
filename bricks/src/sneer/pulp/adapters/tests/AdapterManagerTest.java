package sneer.pulp.adapters.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.adapters.AdapterManager;
import wheel.lang.Functor;

public class AdapterManagerTest extends TestThatIsInjected {
	
	static class A {
	}
	
	static class B {
	}
	
	static class C extends B {
	}
	
	static interface Named {
		String name();
	}
	
	@Inject
	private AdapterManager _adapterManager;
	
	@Test
	public void testRegisterAdapter() {
		
		_adapterManager.register(String.class, Named.class, new Functor<String, Named>() {
			@Override
			public Named evaluate(final String s) {
				return new Named() {
					public String name() {
						return s;
					}
				};
			}
		});
		
		Named named = _adapterManager.adapterFor("foo", Named.class);
		assertNotNull(named);
		assertSame("foo", named.name());
	}

}
