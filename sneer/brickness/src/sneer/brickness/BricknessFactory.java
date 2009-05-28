package sneer.brickness;

import sneer.brickness.impl.BricknessImpl;

public class BricknessFactory {

	public static Brickness newBrickContainer(Object... bindings) {
		return new BricknessImpl(bindings);
	}

	public static Brickness newBrickContainerWithApiClassLoader(ClassLoader apiClassLoader, Object... bindings) {
		return new BricknessImpl(apiClassLoader, bindings);
	}

}
