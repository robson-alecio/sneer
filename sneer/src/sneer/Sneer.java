package sneer;

import static sneer.kernel.SneerDirectories.logDirectory;

import java.io.File;
import java.io.FileNotFoundException;

import sneer.kernel.NameChange;
import sneer.kernel.install.Installer;
import wheel.io.Log;
import wheel.io.ui.User;
import wheel.io.ui.User.Action;
import wheel.lang.Threads;

public class Sneer {


	private User _user;


	public Sneer() {
		try {
			tryToRun();
		} catch (Throwable t) {
			Log.log(t);
		}
	}

	private void tryToRun() throws Exception {
		_user = new User(Sneer.class.getResource("/sneer/gui/traymenu/yourIconGoesHere.png"));
		_user.acknowledgeNotification("Hello World!");
		
		new Installer(_user);
		tryToRedirectLogToSneerLogFile();

		new NameChange(_user, false);
		
		_user.addAction(nameChangeAction());
		_user.addAction(exitAction());

		while (true) Threads.sleepWithoutInterruptions(5000);
	}

	
	private Action nameChangeAction() {
		return new Action(){

			public String caption() {
				return "Change your Name";
			}

			public void run() {
				new NameChange(_user, true);
			}};
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
