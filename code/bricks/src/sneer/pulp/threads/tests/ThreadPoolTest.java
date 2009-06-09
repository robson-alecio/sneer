package sneer.pulp.threads.tests;

import static org.junit.Assert.assertSame;
import static sneer.commons.environments.Environments.my;

import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.brickness.testsupport.BrickTestRunner;
import sneer.commons.environments.Bindings;
import sneer.commons.environments.Environment;
import sneer.commons.environments.EnvironmentUtils;
import sneer.commons.environments.Environments;
import sneer.pulp.threads.Stepper;
import sneer.pulp.threads.Threads;

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
