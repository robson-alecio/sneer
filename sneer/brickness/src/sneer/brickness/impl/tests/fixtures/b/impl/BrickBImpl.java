package sneer.brickness.impl.tests.fixtures.b.impl;

import static sneer.commons.environments.Environments.my;
import sneer.brickness.impl.tests.fixtures.a.BrickA;
import sneer.brickness.impl.tests.fixtures.b.BrickB;

public class BrickBImpl implements BrickB {
	{
		System.setProperty("BrickB.classLoader", "" + getClass().getClassLoader().hashCode());
		my(BrickA.class).setProperty("BrickB was here!");
	}
}
