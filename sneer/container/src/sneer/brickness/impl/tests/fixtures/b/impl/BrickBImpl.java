package sneer.brickness.tests.fixtures.b.impl;

import static sneer.commons.environments.Environments.my;
import sneer.brickness.tests.fixtures.a.BrickA;
import sneer.brickness.tests.fixtures.b.BrickB;

public class BrickBImpl implements BrickB {
	{
		System.setProperty("BrickB.classLoader", getClass().getClassLoader().toString());
		my(BrickA.class).setProperty("BrickB was here!");
	}
}
