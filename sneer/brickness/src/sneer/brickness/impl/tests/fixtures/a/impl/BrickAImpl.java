package sneer.brickness.impl.tests.fixtures.a.impl;

import sneer.brickness.impl.tests.fixtures.a.BrickA;

class BrickAImpl implements BrickA {
	
	{
		System.setProperty("BrickA.ran", "true");
		System.setProperty("BrickA.classLoader", "" + getClass().getClassLoader().hashCode());
	}

	@Override
	public void setProperty(String value) {
		System.setProperty("BrickA.property", value);
	}
}
