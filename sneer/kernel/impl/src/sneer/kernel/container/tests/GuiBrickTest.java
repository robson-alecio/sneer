package sneer.kernel.container.tests;

import static org.junit.Assert.assertSame;

import javax.swing.SwingUtilities;

import org.junit.Test;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.tests.impl.SomeGuiBrickImpl;
import wheel.lang.ByRef;

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
