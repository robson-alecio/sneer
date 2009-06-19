package sneer.foundation.brickness.impl.tests.fixtures.a.impl;

import sneer.foundation.brickness.impl.tests.fixtures.a.BrickA;

class BrickAImpl implements BrickA {
	
	{
		System.setProperty("BrickA.ran", "true");
		System.setProperty("BrickA.classLoader", "" + getClass().getClassLoader().hashCode());
		System.setProperty("BrickA.lib.classLoader", "" + libClassLoaderHash());
	}

	@Override
	public void setProperty(String value) {
		System.setProperty("BrickA.property", value);
	}

	private int libClassLoaderHash() {
		try {
			Class<?> lib = getClass().getClassLoader().loadClass("foo.ClassInLib");
			return lib.newInstance().getClass().getClassLoader().hashCode();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}
