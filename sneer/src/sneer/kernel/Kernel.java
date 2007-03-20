package sneer.kernel;

import static sneer.kernel.SneerDirectories.logDirectory;

import java.io.File;
import java.io.FileNotFoundException;

import sneer.kernel.install.Installer;
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

	private void tryToRun() throws Exception {
		User user = new User(Kernel.class.getResource("yourIconGoesHere.png"));

		new Installer(user);
		tryToRedirectLogToSneerLogFile();

		user.addAction(exitAction());
		while (true) Threads.sleepWithoutInterruptions(5000);
	}

	
	private void tryToRedirectLogToSneerLogFile() throws FileNotFoundException {
		logDirectory().mkdir();
		Log.redirectTo(new File(logDirectory(), "log.txt"));
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
