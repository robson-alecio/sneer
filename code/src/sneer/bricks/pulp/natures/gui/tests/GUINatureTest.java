package sneer.bricks.pulp.natures.gui.tests;

import static sneer.foundation.environments.Environments.my;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.junit.Assert;
import org.junit.Test;

import sneer.bricks.pulp.natures.gui.tests.fixtures.SomeGuiBrick;
import sneer.foundation.brickness.Brickness;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.Environments;

public class GUINatureTest extends Assert {
	
	Environment subject = Brickness.newBrickContainer();
	
	
	@Test
	public void invocationHappensInTheSwingThread() {
		Environments.runWith(subject, new Runnable() { @Override public void run() {
			
			assertTrue(isGuiThread(my(SomeGuiBrick.class).currentThread()));
			
		}});
	}
	
	@Test
	public void listenerInvocationHappensInBricknessEnvironment() {
		Environments.runWith(subject, new Runnable() { @Override public void run() {
			final ActionListener listener = my(SomeGuiBrick.class).listenerFor(subject);
			Environments.runWith(emptyEnvironment(), new Runnable() { @Override public void run() {
				listener.actionPerformed(new ActionEvent(this, 0, null));
			}});
		}});
	}
	
	@Test
	public void invocationHappensInBricknessEnvironment() {
		Environments.runWith(subject, new Runnable() { @Override public void run() {
			
			assertSame(subject, my(SomeGuiBrick.class).currentEnvironment());
			
		}});
	}
	
	
	
	
	@Test
	public void invocationInTheSwingThreadForVoidMethod() {
		Environments.runWith(subject, new Runnable() { @Override public void run() {
			assertFalse(isGuiThread(Thread.currentThread()));
			my(SomeGuiBrick.class).run(new Runnable() { @Override public void run() {
				assertTrue(isGuiThread(Thread.currentThread()));
			}});
			
		}});
	}

	private Environment emptyEnvironment() {
		return new Environment() { @Override public <T> T provide(Class<T> intrface) {
			return null;
		}};
	}

	private boolean isGuiThread(Thread thread) {
		return thread.getName().contains("AWT");
	}
	

}
