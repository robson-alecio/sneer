package wheel.io.ui;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import wheel.io.Logger;

public class GuiThread {
	
	public static void invokeLater(Runnable runnable) { //Fix This method is called sometimes from swing's thread and other times from aplication's thread. Split the caller method (if it is possible), and delete this 'invokeLater' method.
		try {
			if( SwingUtilities.isEventDispatchThread())
				Logger.log("GuiThread.invokeLather:");
			assertNotInGuiThread();
		} catch (RuntimeException e) {
			Logger.log(e);
		}
		SwingUtilities.invokeLater(runnable);
	}
	
	public static void strictInvokeAndWait(Runnable runnable) { //Fix Calling this from brick code is no longer necessary after the container is calling gui brick code only in the Swing thread.
		assertNotInGuiThread();
		try {
			SwingUtilities.invokeAndWait(runnable);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
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
}