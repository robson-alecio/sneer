package sneer.kernel.container.tests;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import javax.swing.SwingUtilities;

import org.junit.Test;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.tests.impl.SomeGuiBrickImpl;
import wheel.io.ui.TimeboxedEventQueue;
import wheel.lang.ByRef;
import wheel.lang.exceptions.TimeIsUp;

public class GuiBrickTest {
	
	@Test
	public void testGuiBrickProducedByConventionRunsInSwingThread() throws Exception {
		final Container container = ContainerUtils.newContainer();
		final SomeGuiBrick brick = container.provide(SomeGuiBrick.class);
		assertSame(swingThread(), brick.guiBrickThread());
	}

	@Test
	public void testInjectedGuiBrickRunsInSwingThread() throws Exception {
		final Container container = ContainerUtils.newContainer(new SomeGuiBrickImpl());
		final SomeGuiBrick brick = container.provide(SomeGuiBrick.class);
		assertSame(swingThread(), brick.guiBrickThread());
	}
	
	@Test
	public void testGuiBrickRunsInsideTimebox() throws Exception {
		int timeoutForGuiEvents = 10;
		TimeboxedEventQueue.startQueueing(null, timeoutForGuiEvents);

		try {
			runInsideTimebox();
		} finally {
			TimeboxedEventQueue.stopQueueing();
		}
	}


	private void runInsideTimebox() {
		final Container container = ContainerUtils.newContainer();
		final SomeGuiBrick brick = container.provide(SomeGuiBrick.class);
		try {
			brick.slowMethod();
		} catch (TimeIsUp expected) {
			return;
		}
		fail("timebox should have stopped the method");
	}

	@Test
	public void testNonGuiBrickRunsInCurrentThread() throws Exception {
		final SomeVanillaBrick brick = ContainerUtils.newContainer().provide(SomeVanillaBrick.class);
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
