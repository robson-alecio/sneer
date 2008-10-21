package wheel.io.ui;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

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
			SwingUtilities.invokeAndWait(runnable);
		} catch (InterruptedException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (InvocationTargetException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}

	public static void strictInvokeLater(Runnable runnable) { //Fix Calling this from brick code is no longer necessary after the container is calling gui brick code only in the Swing thread.
		assertNotInGuiThread();
		SwingUtilities.invokeLater(runnable);
	}

	public static void assertInGuiThread() {
		if (!SwingUtilities.isEventDispatchThread()) throw new IllegalStateException("Should be running in the GUI thread."); 
	}

	private static void assertNotInGuiThread() {
		if (SwingUtilities.isEventDispatchThread()) throw new IllegalStateException("Should NOT be running in the GUI thread."); 
	}

	public static void invokeLater(Runnable runnable) {
		SwingUtilities.invokeLater(runnable);
	}
}