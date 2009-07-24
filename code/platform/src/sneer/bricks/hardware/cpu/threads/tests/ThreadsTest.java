package sneer.bricks.hardware.cpu.threads.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.Environments;

public class ThreadsTest extends BrickTest {

	private final Threads _subject = my(Threads.class);

	@Test (timeout = 2000)
	public void environmentIsPropagatedToSteppables() throws Exception {
		final Environment environment = my(Environment.class);
		final Latch latch = _subject.newLatch();

		@SuppressWarnings("unused")
		final Object refToAvoidGc = _subject.startStepping(new Steppable() { @Override public void step() {
			assertSame(environment, Environments.my(Environment.class));
			latch.open();
		}});
		
		latch.waitTillOpen();
	}

	@Test (timeout = 2000)
	public void threadsAreCrashed() {
		Thread thread = new Thread() { @Override public void run(){
			_subject.crashAllThreads();
		}};
		thread.start();

		_subject.waitUntilCrash();
	}
	
	@Test (timeout = 2000)
	public void crashHandlersAreNotified() {
		
		final Latch crashingLatch = my(Threads.class).newLatch();
		@SuppressWarnings("unused")
		Contract crashingContract = my(Threads.class).crashing().addPulseReceiver(crashingLatch);
		
		Thread thread = new Thread() { @Override public void run(){
			_subject.crashAllThreads();
		}};
		thread.start();

		_subject.waitUntilCrash();
		
		crashingLatch.waitTillOpen();
	}

}
