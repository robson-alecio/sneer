package sneer.bricks.pulp.natures.gui.tests.fixtures;

import java.awt.event.*;

import sneer.bricks.pulp.natures.gui.GUI;
import sneer.foundation.brickness.Brick;
import sneer.foundation.environments.Environment;

// TODO: callbacks
// TODO: static methods
// TODO: nature inheritance (annotation Instrument interface for instance)
// TODO: methods declaring checked exceptions
// TODO: constructors
@Brick(GUI.class)
public interface SomeGuiBrick {

	Thread currentThread();

	Environment currentEnvironment();

	void run(Runnable runnable);

	ActionListener listenerFor(Environment expectedEnvironment);

}
