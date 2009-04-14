package sneer.pulp.natures.gui.tests;

import static sneer.commons.environments.Environments.my;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import sneer.brickness.impl.Brickness;
import sneer.commons.environments.Environments;
import sneer.commons.lang.ByRef;
import sneer.hardware.gui.guithread.GuiThread;
import sneer.pulp.natures.gui.GUI;
import sneer.pulp.natures.gui.tests.fixtures.SomeGuiBrick;
import wheel.io.Jars;

public class GUINatureTest extends Assert {
	
	Brickness subject = new Brickness();
	
	@Before
	public void placeBricks() {
		placeBrick(GuiThread.class);
		placeBrick(GUI.class);
		placeBrick(SomeGuiBrick.class);
	}
	
	@Test
	public void invocationHappensInTheSwingThread() {
		Environments.runWith(subject.environment(), new Runnable() { @Override public void run() {
			
			assertSame(swingThread(), my(SomeGuiBrick.class).currentThread());
			
		}});
	}
	
	@Test
	public void invocationHappensInBricknessEnvironment() {
		Environments.runWith(subject.environment(), new Runnable() { @Override public void run() {
			
			assertSame(subject.environment(), my(SomeGuiBrick.class).currentEnvironment());
			
		}});
	}
	
	
	@Test
	public void invocationInTheSwingThreadForVoidMethod() {
		Environments.runWith(subject.environment(), new Runnable() { @Override public void run() {
			
			my(SomeGuiBrick.class).run(new Runnable() { @Override public void run() {
				assertSame(swingThread(), Thread.currentThread());
			}});
			
		}});
	}

	private void placeBrick(final Class<?> brick) {
		subject.placeBrick(Jars.classpathRootFor(brick), brick.getName());
	}
	
	private Thread swingThread() {
		final ByRef<Thread> swingThread = ByRef.newInstance();
		try {
			my(GuiThread.class).invokeAndWait(new Runnable() { @Override public void run() {
				swingThread.value = Thread.currentThread();
			}});
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		return swingThread.value;
	}

}
