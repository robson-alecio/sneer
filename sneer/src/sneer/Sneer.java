package sneer;

import static sneer.strap.SneerDirectories.validNumber;

import java.io.File;

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

		new NameAcquisition(_user);
	}


	private void tryToDownloadNextVersion() throws Exception {
		File mainApp = SneerDirectories.findNewestMainApp();
		int currentVersion = validNumber(mainApp.getName());
		new VersionUpdateAttempt(currentVersion + 1, _user);
	}
}
