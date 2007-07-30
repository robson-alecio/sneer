package wheel.io.ui;

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