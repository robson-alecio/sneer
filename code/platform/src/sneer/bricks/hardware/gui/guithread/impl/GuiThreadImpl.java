package sneer.bricks.hardware.gui.guithread.impl;

import static sneer.foundation.environments.Environments.my;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.Environments;

class GuiThreadImpl implements GuiThread {
	
	private Set<Thread> _threadsThatShouldNotWaitForGui = new HashSet<Thread>();

	@Override
	public void invokeAndWaitForWussies(final Runnable runnable) { //Fix This method is called sometimes from swing's thread and other times from aplication's thread. Split the caller method (if it is possible), and delete this method.
		if(SwingUtilities.isEventDispatchThread())
			runnable.run();
		else
			invokeAndWait(runnable);
	}

	private void invokeAndWait(final Environment environment, final Runnable runnable) { //Fix Calling this from brick code is no longer necessary after the container is calling gui brick code only in the Swing thread.
		assertNotInGuiThread();
		assertThreadCanWaitForGui();
		try {
			SwingUtilities.invokeAndWait(envolve(environment, runnable));
		} catch (InterruptedException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (InvocationTargetException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}
	
	@Override
	public void invokeAndWait(final Runnable runnable) { //Fix Calling this from brick code is no longer necessary after the container is calling gui brick code only in the Swing thread.
		invokeAndWait(my(Environment.class), runnable);
	}

	@Override
	public void invokeLater(Runnable runnable) {
		SwingUtilities.invokeLater(envolve(runnable));
	}

	@Override
	public void assertInGuiThread() {
		if (!SwingUtilities.isEventDispatchThread()) throw new IllegalStateException("Should be running in the GUI thread."); 
	}

	@Override
	public void assertNotInGuiThread() {
		if (SwingUtilities.isEventDispatchThread()) throw new IllegalStateException("Should NOT be running in the GUI thread."); 
	}
	
	private Runnable envolve(final Runnable delegate) {
		return envolve(my(Environment.class), delegate);
	}

	private Runnable envolve(final Environment environment, final Runnable delegate) {
		return new Runnable() { @Override public void run() {
			Environments.runWith(environment, delegate);
		}};
	}

	private void assertThreadCanWaitForGui() {
		if (_threadsThatShouldNotWaitForGui.contains(Thread.currentThread())) throw new IllegalStateException("The current thread should not have to wait for the GUI thread."); 
	}

}