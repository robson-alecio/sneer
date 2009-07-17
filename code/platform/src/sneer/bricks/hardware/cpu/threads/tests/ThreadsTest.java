package sneer.bricks.hardware.cpu.threads.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Stepper;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.Environments;

public class ThreadsTest extends BrickTest {

	private final Threads _subject = my(Threads.class);

	@Test (timeout = 2000)
	public void environmentIsPropagated() throws Exception {
		final Environment environment = my(Environment.class);
		final Latch latch = _subject.newLatch();

		final Stepper refToAvoidGc = new Stepper() { @Override public boolean step() {
			assertSame(environment, Environments.my(Environment.class));
			latch.open();
			return false;
		}};

		_subject.registerStepper(refToAvoidGc);
		
		latch.await();
	}

	@Test (timeout = 2000)
	public void threadsAreCrashed() {
		Thread thread = new Thread() { @Override public void run(){
			_subject.crashAllThreads();
		}};
		thread.start();

		_subject.waitUntilCrash();
	}

}
