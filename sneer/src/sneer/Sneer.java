package sneer;

import static sneer.strap.SneerDirectories.validNumber;

import java.io.File;
import java.io.IOException;

import sneer.strap.SneerDirectories;
import sneer.strap.VersionUpdateAttempt;
import wheelexperiments.Log;


public class Sneer {
	
	public Sneer() {
		try {
			tryToRun();
		} catch (Throwable t) {
			Log.log(t);
		}
	}


	private void tryToRun() throws IOException {
		int TODO_Optimization_DoInParallelThread;
		tryToDownloadNextVersion();

		new NameAcquisition();
	}


	private void tryToDownloadNextVersion() throws IOException {
		File mainApp = SneerDirectories.findNewestMainApp();
		int currentVersion = validNumber(mainApp.getName());
		new VersionUpdateAttempt(currentVersion + 1);
	}

	
}
