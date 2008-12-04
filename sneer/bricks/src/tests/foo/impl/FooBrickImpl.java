package tests.foo.impl;

import tests.bar.BarBrick;
import tests.foo.FooBrick;
import static wheel.lang.Environments.my;

class FooBrickImpl implements FooBrick {
	
	final BarBrick _bar = my(BarBrick.class);

	@Override
	public void pingBar() {
		_bar.ping();
	}

}
