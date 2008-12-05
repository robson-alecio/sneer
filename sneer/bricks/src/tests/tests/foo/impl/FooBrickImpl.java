package tests.tests.foo.impl;

import static wheel.lang.Environments.my;
import tests.tests.bar.BarBrick;
import tests.tests.foo.FooBrick;

class FooBrickImpl implements FooBrick {
	
	final BarBrick _bar = my(BarBrick.class);

	@Override
	public BarBrick bar() {
		return _bar;
	}

}
