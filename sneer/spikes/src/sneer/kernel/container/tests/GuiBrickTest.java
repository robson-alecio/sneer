package sneer.kernel.container.tests;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import javax.swing.SwingUtilities;

import org.junit.Ignore;
import org.junit.Test;

import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.commons.lang.ByRef;
import sneer.commons.lang.exceptions.NotImplementedYet;
import sneer.kernel.container.ContainerOld;
import sneer.kernel.container.ContainersOld;
import wheel.io.ui.TimeboxedEventQueue;
import wheel.lang.exceptions.TimeIsUp;

@Ignore
public class GuiBrickTest {
	
	@Test
	public void guiBrickRunsInSwingThread() throws Exception {
		final ContainerOld container = ContainersOld.newContainer();
		final SomeGuiBrick brick = container.provide(SomeGuiBrick.class);
		assertSame(swingThread(), brick.currentThread());
	}
	
	@Test
	public void guiBrickRunsInContainerEnvironment() throws Exception {
		final ContainerOld container = ContainersOld.newContainer();
		final SomeGuiBrick brick = container.provide(SomeGuiBrick.class);
		assertSame(container, brick.currentEnvironment());
	}

	@Test
	public void injectedGuiBrickRunsInSwingThread() throws Exception {
		final ContainerOld container = ContainersOld.newContainer(new SomeGuiBrick() {			
			@Override
			public Thread currentThread() {
				return Thread.currentThread();
			}

			@Override
			public void slowMethod() {
				throw new IllegalStateException();
			}

			@Override
			public Environment currentEnvironment() {
				throw new IllegalStateException();
			}
		});
		final SomeGuiBrick brick = container.provide(SomeGuiBrick.class);
		assertSame(swingThread(), brick.currentThread());
	}

	@Test
	public void guiBrickCallbacksComeInSwingThread() throws Exception {
		throw new NotImplementedYet();
	}
	
	@Test
	public void testGuiBrickRunsInsideTimebox() throws Exception {
		Environments.runWith(ContainersOld.newContainer(), new Runnable() { @Override public void run() {
			
			int timeoutForGuiEvents = 10;
			TimeboxedEventQueue.startQueueing(timeoutForGuiEvents);
	
			try {
				runInsideTimebox();
			} finally {
				TimeboxedEventQueue.stopQueueing();
			}
			
		}});
	}


	private void runInsideTimebox() {
		final ContainerOld container = ContainersOld.newContainer();
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
		final SomeVanillaBrick brick = ContainersOld.newContainer().provide(SomeVanillaBrick.class);
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
