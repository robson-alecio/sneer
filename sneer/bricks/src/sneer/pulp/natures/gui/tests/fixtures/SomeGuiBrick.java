package sneer.pulp.natures.gui.tests.fixtures;

import sneer.brickness.Brick;
import sneer.commons.environments.Environment;
import sneer.pulp.natures.gui.GUI;

// TODO: callbacks
// TODO: static methods
// TODO: nature inheritance (annotation Instrument interface for instance)
// TODO: methods declaring checked exceptions
// TODO: constructors
// TODO: methods with primitive return type
// TODO: methods taking arguments
@Brick(GUI.class)
public interface SomeGuiBrick {

	Thread currentThread();

	Environment currentEnvironment();

	void run(Runnable runnable);

}
