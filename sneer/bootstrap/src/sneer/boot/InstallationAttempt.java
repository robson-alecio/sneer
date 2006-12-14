package sneer.boot;

import static sneer.boot.SneerDirectories.logDirectory;
import static sneer.boot.SneerDirectories.sneerDirectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import wheelexperiments.Log;
import wheelexperiments.environment.ui.User;

public class InstallationAttempt {

	InstallationAttempt(User user) throws IOException {
		if (!sneerDirectory().exists()) {
			new InstallationDialog(user);
			tryToCreateSneerDirectory();
		}
		tryToRedirectLog();
		new VersionUpdateAttempt(1);
	}

	private static void tryToCreateSneerDirectory() throws IOException {
		if (!sneerDirectory().mkdir())
			throw new IOException("Unable to create Sneer directory\n" + sneerDirectory());
	}

	private static void tryToRedirectLog() throws FileNotFoundException {
		logDirectory().mkdir();
		File logfile = new File(logDirectory(), "log.txt");
		
		Log.redirectTo(new FileOutputStream(logfile));
	}

}
