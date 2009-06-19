package sneer.bricks.pulp.natures.gui.tests.fixtures.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.event.*;

import org.junit.*;

import sneer.bricks.pulp.natures.gui.tests.fixtures.*;
import sneer.foundation.environments.*;

class SomeGuiBrickImpl implements SomeGuiBrick {

	@Override
	public Thread currentThread() {
		return Thread.currentThread();
	}

	@Override
	public Environment currentEnvironment() {
		return my(Environment.class);
	}

	@Override
	public void run(Runnable runnable) {
		runnable.run();
	}

	@Override
	public ActionListener listenerFor(final Environment expectedEnvironment) {
		return new ActionListener() { @Override public void actionPerformed(ActionEvent e) {
			Assert.assertSame(expectedEnvironment, currentEnvironment());
		}};
	}
	
}