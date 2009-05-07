package sneer.brickness.impl;

import sneer.brickness.Brickness;
import sneer.commons.environments.*;
import static sneer.commons.environments.Environments.my;
import wheel.io.*;

public class AutoBricknessEnvironment implements Environment {

	@Override
	public <T> T provide(Class<T> intrface) {
		final Brickness brickness = my(Brickness.class);
		brickness.placeBrick(Jars.classpathRootFor(intrface), intrface.getName());
		return brickness.environment().provide(intrface);
	}


}
