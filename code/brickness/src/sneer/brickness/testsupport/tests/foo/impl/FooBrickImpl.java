package sneer.brickness.testsupport.tests.foo.impl;

import static sneer.commons.environments.Environments.my;
import sneer.brickness.testsupport.tests.bar.BarBrick;
import sneer.brickness.testsupport.tests.foo.FooBrick;

class FooBrickImpl implements FooBrick {
	
	final BarBrick _bar = my(BarBrick.class);

	@Override
	public BarBrick bar() {
		return _bar;
	}

}
