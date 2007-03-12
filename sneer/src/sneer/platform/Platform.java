package sneer.platform;

import wheel.io.Log;

public class Platform {


	public Platform() {
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
