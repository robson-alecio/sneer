package sneer.foundation.brickness.impl.tests.fixtures.b.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.foundation.brickness.impl.tests.fixtures.a.BrickA;
import sneer.foundation.brickness.impl.tests.fixtures.b.BrickB;

public class BrickBImpl implements BrickB {
	{
		System.setProperty("BrickB.classLoader", "" + getClass().getClassLoader().hashCode());
		System.setProperty("BrickB.lib.classLoader", "" + libClassLoaderHash());

		my(BrickA.class).setProperty("BrickB was here!");
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
