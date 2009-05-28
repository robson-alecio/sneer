package sneer.brickness;

import sneer.brickness.impl.BricknessImpl;
import sneer.commons.environments.Environment;

public class Brickness {

	public static Environment newBrickContainer(Object... bindings) {
		return new BricknessImpl(bindings);
	}

	public static Environment newBrickContainerWithApiClassLoader(ClassLoader apiClassLoader, Object... bindings) {
		return new BricknessImpl(apiClassLoader, bindings);
	}

}
