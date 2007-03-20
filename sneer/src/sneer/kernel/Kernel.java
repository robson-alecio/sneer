package sneer.kernel;

import wheel.io.Log;

public class Kernel {


	public Kernel() {
		try {
			tryToRun();
		} catch (Throwable t) {
			Log.log(t);
		}
	}

	private void tryToRun() {
		int TODO;
	}
}
