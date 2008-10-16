package sneer.kernel.container.tests;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import javax.swing.SwingUtilities;

import org.junit.Test;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.tests.impl.SomeGuiBrickImpl;
import sneer.pulp.clock.Clock;
import wheel.lang.ByRef;
import wheel.lang.Daemon;
import wheel.lang.Threads;
import wheel.lang.exceptions.TimeIsUp;

public class GuiBrickTest {
	
	@Test
	public void testGuiBrickProducedByConventionRunsInSwingThread() throws Exception {
		final Container container = ContainerUtils.newContainer();
		final SomeGuiBrick brick = container.produce(SomeGuiBrick.class);
		assertSame(swingThread(), brick.guiBrickThread());
	}

	@Test
	public void testInjectedGuiBrickRunsInSwingThread() throws Exception {
		final Container container = ContainerUtils.newContainer(new SomeGuiBrickImpl());
		final SomeGuiBrick brick = container.produce(SomeGuiBrick.class);
		assertSame(swingThread(), brick.guiBrickThread());
	}
	
	@Test
	public void testGuiBrickRunsInsideTimebox() throws Exception {
		final Container container = ContainerUtils.newContainer();
		final SomeGuiBrick brick = container.produce(SomeGuiBrick.class);
		final Clock clock = container.produce(Clock.class);
		advanceClockInSeparateThread(clock);
		try {
			brick.slowMethod();
		} catch (TimeIsUp e) {
			return;
		}
		fail("timebox should have stopped the method");
	}

	private void advanceClockInSeparateThread(final Clock clock) {
		new Daemon("Timebox") { @Override public void run() {
			Threads.sleepWithoutInterruptions(500);
			clock.advanceTime(5000);
		}};
	}
	

	@Test
	public void testNonGuiBrickRunsInCurrentThread() throws Exception {
		final SomeVanillaBrick brick = ContainerUtils.newContainer().produce(SomeVanillaBrick.class);
		assertSame(Thread.currentThread(), brick.brickThread());
	}
	
	private Thread swingThread() throws Exception {
		final ByRef<Thread> swingThread = ByRef.newInstance();
		SwingUtilities.invokeAndWait(new Runnable() { @Override public void run() {
			swingThread.value = Thread.currentThread();
		}});
		return swingThread.value;
	}
}
