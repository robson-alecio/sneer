package wheel.io.ui;

import java.lang.reflect.InvocationTargetException;
import static sneer.commons.environments.Environments.my;


import javax.swing.SwingUtilities;

import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;

public class GuiThread {
	
	public static void invokeAndWait(final Runnable runnable) { //Fix This method is called sometimes from swing's thread and other times from aplication's thread. Split the caller method (if it is possible), and delete this method.
		if(SwingUtilities.isEventDispatchThread())
			runnable.run();
		else
			strictInvokeAndWait(runnable);
	}
	
	public static void strictInvokeAndWait(final Runnable runnable) { //Fix Calling this from brick code is no longer necessary after the container is calling gui brick code only in the Swing thread.
		assertNotInGuiThread();
		try {
			SwingUtilities.invokeAndWait(envolve(runnable));
		} catch (InterruptedException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (InvocationTargetException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}

	public static void strictInvokeLater(Runnable runnable) { //Fix Calling this from brick code is no longer necessary after the container is calling gui brick code only in the Swing thread.
		assertNotInGuiThread();
		SwingUtilities.invokeLater(envolve(runnable));
	}

	public static void assertInGuiThread() {
		if (!SwingUtilities.isEventDispatchThread()) throw new IllegalStateException("Should be running in the GUI thread."); 
	}

	private static void assertNotInGuiThread() {
		if (SwingUtilities.isEventDispatchThread()) throw new IllegalStateException("Should NOT be running in the GUI thread."); 
	}

	public static void invokeLater(Runnable runnable) {
		SwingUtilities.invokeLater(envolve(runnable));
	}

	private static Runnable envolve(final Runnable delegate) {
		final Environment environment = my(Environment.class);
		return new Runnable() { @Override public void run() {
			Environments.runWith(environment, delegate);
		}};
	}
}