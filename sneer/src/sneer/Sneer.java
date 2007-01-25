package sneer;

import static sneer.strap.SneerDirectories.validNumber;

import java.io.File;
import java.io.FileNotFoundException;

import sneer.strap.SneerDirectories;
import sneer.strap.VersionUpdateAttempt;
import wheelexperiments.Log;
import wheelexperiments.environment.ui.User;


public class Sneer {
	
	private User _user = TestMode.createUser(Sneer.class.getResource("logo16.png"));


	public Sneer() {
		try {
			tryToRun();
		} catch (Throwable t) {
			Log.log(t);
		}
	}

	private void tryToRun() throws Exception {		
		int TODO_Optimization_DoInParallelThread;
		tryToDownloadNextVersion();

		new NameChange(_user, false);
		
		registerUserActions();
		
		while (true) Thread.sleep(5000);
	}

	
	private void registerUserActions() {
		_user.addActions(nameChangeAction());
		_user.addActions(exitAction());
	}

	private User.Action exitAction() {
		return new User.Action() {

			public void run() {
				System.exit(0);
			}

			public String getCaption() {
				return "Sair";
			}
		};
	}

	private User.Action nameChangeAction() {
		return new User.Action() {

			public void run() {
				try {
					new NameChange(_user, true);
				} catch (FileNotFoundException e) {
					Log.log(e);
				}
			}

			public String getCaption() {
				return "Mudar Meu Nome";
			}
		};
	}

	private void tryToDownloadNextVersion() throws Exception {
		File mainApp = SneerDirectories.findNewestMainApp();
		int currentVersion = validNumber(mainApp.getName());
		new VersionUpdateAttempt(currentVersion + 1, _user);
	}
}
