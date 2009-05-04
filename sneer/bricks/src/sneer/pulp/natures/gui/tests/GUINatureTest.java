package sneer.pulp.natures.gui.tests;

import static sneer.commons.environments.Environments.my;

import java.awt.event.*;

import org.junit.*;

import sneer.brickness.impl.*;
import sneer.commons.environments.*;
import sneer.commons.lang.*;
import sneer.hardware.gui.guithread.*;
import sneer.pulp.natures.gui.*;
import sneer.pulp.natures.gui.tests.fixtures.*;
import wheel.io.*;

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
	public void listenerInvocationHappensInBricknessEnvironment() {
		Environments.runWith(subject.environment(), new Runnable() { @Override public void run() {
			final ActionListener listener = my(SomeGuiBrick.class).listenerFor(subject.environment());
			Environments.runWith(emptyEnvironment(), new Runnable() { @Override public void run() {
				listener.actionPerformed(new ActionEvent(this, 0, null));
			}});
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
			assertNotSame(swingThread(), Thread.currentThread());
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

	private Environment emptyEnvironment() {
		return new Environment() { @Override public <T> T provide(Class<T> intrface) {
			return null;
		}};
	}

}
