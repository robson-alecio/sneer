package sneer.pulp.natures.gui.tests;

import static sneer.commons.environments.Environments.my;

import javax.swing.SwingUtilities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import sneer.brickness.impl.Brickness;
import sneer.commons.environments.Environments;
import sneer.commons.lang.ByRef;
import sneer.pulp.natures.gui.GUI;
import sneer.pulp.natures.gui.tests.fixtures.SomeGuiBrick;
import wheel.io.Jars;

@Ignore
public class GUINatureTest extends Assert {
	
	Brickness subject = new Brickness();
	
	@Before
	public void placeBricks() {
		placeBrick(GUI.class);
		placeBrick(SomeGuiBrick.class);
	}
	
	@Test
	public void invocationHappensInBricknessEnvironment() {
		Environments.runWith(subject.environment(), new Runnable() { @Override public void run() {
			try {
				assertSame(subject.environment(), my(SomeGuiBrick.class).currentEnvironment());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}});
	}
	
	@Test
	public void invocationHappensInTheSwingThread() {
		Environments.runWith(subject.environment(), new Runnable() { @Override public void run() {
			try {
				assertSame(swingThread(), my(SomeGuiBrick.class).currentThread());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}});
	}

	private void placeBrick(final Class<?> brick) {
		subject.placeBrick(Jars.classpathRootFor(brick), brick.getName());
	}
	
	private Thread swingThread() throws Exception {
		final ByRef<Thread> swingThread = ByRef.newInstance();
		SwingUtilities.invokeAndWait(new Runnable() { @Override public void run() {
			swingThread.value = Thread.currentThread();
		}});
		return swingThread.value;
	}

}
