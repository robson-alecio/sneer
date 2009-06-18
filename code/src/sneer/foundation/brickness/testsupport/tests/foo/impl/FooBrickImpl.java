package sneer.foundation.brickness.testsupport.tests.foo.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.foundation.brickness.testsupport.tests.bar.BarBrick;
import sneer.foundation.brickness.testsupport.tests.foo.FooBrick;

class FooBrickImpl implements FooBrick {
	
	final BarBrick _bar = my(BarBrick.class);

	@Override
	public BarBrick bar() {
		return _bar;
	}

}
