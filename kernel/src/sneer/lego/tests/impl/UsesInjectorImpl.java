package sneer.lego.tests.impl;

import sneer.lego.Inject;
import sneer.lego.Injector;
import sneer.lego.tests.UsesInjector;

public class UsesInjectorImpl implements UsesInjector {

	@Inject
	private static Injector _injector;
	
	@Override
	public Injector injector() {
		return _injector;
	}

}
