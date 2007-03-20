package sneer.kernel;

import wheel.io.Log;
import wheel.io.ui.User;
import wheel.io.ui.User.Action;
import wheel.lang.Threads;

public class Kernel {


	public Kernel() {
		try {
			tryToRun();
		} catch (Throwable t) {
			Log.log(t);
		}
	}

	private void tryToRun() {
		User user = new User(Kernel.class.getResource("yourIconGoesHere.png"));
		
		user.addAction(exitAction());
		
		while (true) Threads.sleepWithoutInterruptions(5000);
	}

	
	private Action exitAction() {
		return new Action(){

			public String caption() {
				return "Exit";
			}

			public void run() {
				System.exit(0);
			}
		};
	}
}
