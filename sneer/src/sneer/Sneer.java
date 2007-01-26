package sneer;

import static sneer.strap.SneerDirectories.validNumber;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import sneer.strap.SneerDirectories;
import sneer.strap.VersionUpdateAttempt;
import wheelexperiments.Log;
import wheelexperiments.environment.ui.User;
import wheelexperiments.environment.ui.User.Action;


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
		
		new IngredientMixup();
		
		int arbitraryLargeNumber = 5000;
		while (true) Thread.sleep(arbitraryLargeNumber);
	}

	
	private void registerUserActions() {
		_user.addAction(nameChangeAction());
		_user.addAction(exitAction());
	}

	private User.Action exitAction() {
		return new User.Action() {
			
			public String caption() {
				return "Sair";
			}

			public void run() {
				System.exit(0);
			}
		};
	}

	private User.Action nameChangeAction() {
		return new User.Action() {
			
			public String caption() {
				return "Mudar Meu Nome...";
			}

			public void run() {
				try {
					new NameChange(_user, true);
				} catch (FileNotFoundException e) {
					Log.log(e);
				}
			}
		};
	}

	private void tryToDownloadNextVersion() throws Exception {
		File mainApp = SneerDirectories.findNewestMainApp();
		int currentVersion = validNumber(mainApp.getName());
		new VersionUpdateAttempt(currentVersion + 1, _user);
	}
}
