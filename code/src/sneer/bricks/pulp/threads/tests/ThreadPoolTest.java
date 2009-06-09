package sneer.bricks.pulp.threads.tests;

import static org.junit.Assert.assertSame;
import static sneer.foundation.commons.environments.Environments.my;

import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.bricks.pulp.threads.Stepper;
import sneer.bricks.pulp.threads.Threads;
import sneer.foundation.brickness.testsupport.BrickTestRunner;
import sneer.foundation.commons.environments.Bindings;
import sneer.foundation.commons.environments.Environment;
import sneer.foundation.commons.environments.EnvironmentUtils;
import sneer.foundation.commons.environments.Environments;

@RunWith(BrickTestRunner.class)
public class ThreadPoolTest {

	private final Threads _subject = my(Threads.class);
	private final Object _binding = new Object();
	private final Object _ranMonitor = new Object();

	private volatile boolean _ran = false;

	@Test (timeout = 2000)
	public void testEnvironmentIsPropagated() throws Exception {
		final Environment testEnvironment = new Bindings(_binding).environment();
		final Environment environment = EnvironmentUtils.compose(testEnvironment, my(Environment.class));

		Environments.runWith(environment, new Runnable() { @Override public void run() {
			final Stepper refToAvoidGc = new Stepper() { @Override public boolean step() {
				assertSame(_binding, Environments.my(Object.class));
				synchronized (_ranMonitor) {
					_ran = true;
					_ranMonitor.notify();
				}
				return false;
			}};

			_subject.registerStepper(refToAvoidGc);
		}});

		synchronized (_ranMonitor) {
			if (!_ran)
				_subject.waitWithoutInterruptions(_ranMonitor);
		}
	}
}
