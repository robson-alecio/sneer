package sneer.platform;

import static sneer.platform.SneerDirectories.validNumber;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import sneer.platform.strap.VersionUpdateAttempt;
import wheel.io.Log;
import wheel.io.ui.User;
import wheel.io.ui.User.Action;


public class Platform {
	
	private User _user = TestMode.createUser(Platform.class.getResource("logo16.png"));


	public Platform() {
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
		
		int TODO;
		//new Recipe();
		
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
		File mainApp = SneerDirectories.latestInstalledPlatformJar();
		int currentVersion = validNumber(mainApp.getName());
		new VersionUpdateAttempt(currentVersion + 1, _user);
	}
}
