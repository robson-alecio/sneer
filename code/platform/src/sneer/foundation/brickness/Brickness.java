package sneer.foundation.brickness;

import sneer.foundation.brickness.impl.BricknessImpl;
import sneer.foundation.environments.Environment;

public class Brickness {

	public static Environment newBrickContainer(Object... bindings) {
		return new BricknessImpl(bindings);
	}

}
