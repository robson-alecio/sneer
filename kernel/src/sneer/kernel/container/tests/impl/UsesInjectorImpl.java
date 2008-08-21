package sneer.kernel.container.tests.impl;

import sneer.kernel.container.Inject;
import sneer.kernel.container.Injector;
import sneer.kernel.container.tests.UsesInjector;

public class UsesInjectorImpl implements UsesInjector {

	@Inject
	private static Injector _injector;
	
	@Override
	public Injector injector() {
		return _injector;
	}

}
