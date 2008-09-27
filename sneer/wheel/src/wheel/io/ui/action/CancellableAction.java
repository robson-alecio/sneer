package wheel.io.ui.action;

import wheel.io.ui.CancelledByUser;

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