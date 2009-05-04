package sneer.pulp.natures.gui.tests.fixtures.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.event.*;

import org.junit.*;

import sneer.commons.environments.*;
import sneer.pulp.natures.gui.tests.fixtures.*;

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