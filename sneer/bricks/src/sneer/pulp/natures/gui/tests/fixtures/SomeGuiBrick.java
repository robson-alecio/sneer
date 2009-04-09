package sneer.pulp.natures.gui.tests.fixtures;

import sneer.brickness.Brick;
import sneer.commons.environments.Environment;
import sneer.pulp.natures.gui.GUI;

// TODO: methods with no return type
// TODO: methods with primitive return type
// TODO: methods taking arguments
// TODO: methods declaring checked exceptions
// TODO: constructors
@Brick(GUI.class)
public interface SomeGuiBrick {

	Thread currentThread();

	Environment currentEnvironment();

}
