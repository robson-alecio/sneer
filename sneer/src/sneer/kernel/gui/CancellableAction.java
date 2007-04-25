package sneer.kernel.gui;

import wheel.io.ui.CancelledByUser;
import wheel.io.ui.TrayIcon.Action;

public abstract class CancellableAction implements Action {

	public void run() {
		try {
			tryToRun();
		} catch (CancelledByUser e) {
			//Fair enough;
		}
		
	}

	protected abstract void tryToRun() throws CancelledByUser;

}