package sneer.container.tests.fixtures.a.impl;

import sneer.container.tests.fixtures.a.BrickA;

class BrickAImpl implements BrickA {
	
	{
		System.setProperty("BrickA.ran", "true");
		System.setProperty("BrickA.classLoader", getClass().getClassLoader().toString());
	}

	@Override
	public void setProperty(String value) {
		System.setProperty("BrickA.property", value);
	}
}
