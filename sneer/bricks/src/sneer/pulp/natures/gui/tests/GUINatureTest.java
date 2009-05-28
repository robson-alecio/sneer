package sneer.pulp.natures.gui.tests;

import static sneer.commons.environments.Environments.my;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.junit.Assert;
import org.junit.Test;

import sneer.brickness.Brickness;
import sneer.brickness.BricknessFactory;
import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.commons.lang.ByRef;
import sneer.hardware.gui.guithread.GuiThread;
import sneer.pulp.natures.gui.tests.fixtures.SomeGuiBrick;

public class GUINatureTest extends Assert {
	
	Brickness subject = BricknessFactory.newBrickContainer();
	
	
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
