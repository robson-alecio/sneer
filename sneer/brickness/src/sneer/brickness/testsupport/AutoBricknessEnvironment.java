package sneer.brickness.testsupport;

import static sneer.commons.environments.Environments.my;
import sneer.brickness.Brickness;
import sneer.commons.environments.Environment;

public class AutoBricknessEnvironment implements Environment {

	@Override
	public <T> T provide(Class<T> intrface) {
		final Brickness brickness = my(Brickness.class);
		brickness.placeBrick(ClassFiles.classpathRootFor(intrface), intrface.getName());
		return brickness.environment().provide(intrface);
	}


}
