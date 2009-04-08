package sneer.pulp.natures.gui.tests.fixtures;

import sneer.brickness.Brick;
import sneer.commons.environments.Environment;
import sneer.pulp.natures.gui.GUI;

@Brick(GUI.class)
public interface SomeGuiBrick {

	Thread currentThread();

	Environment currentEnvironment();

}
