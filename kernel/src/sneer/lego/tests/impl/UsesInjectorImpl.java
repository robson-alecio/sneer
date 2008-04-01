package sneer.lego.tests.impl;

import sneer.lego.Brick;
import sneer.lego.impl.Injector;
import sneer.lego.tests.UsesInjector;

public class UsesInjectorImpl implements UsesInjector {

	@Brick
	private Injector _injector;
	
	@Override
	public Injector injector() {
		return _injector;
	}

}
