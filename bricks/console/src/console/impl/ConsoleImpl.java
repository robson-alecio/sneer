package console.impl;

import wheel.io.ui.CancelledByUser;
import wheel.io.ui.impl.UserImpl;
import console.Console;

public class ConsoleImpl implements Console {

	@Override
	public void out(String message) {
		try {
			new UserImpl("Sneer", null).answer(message);
		} catch (CancelledByUser e) {
			e.printStackTrace();
		}
	}

}
