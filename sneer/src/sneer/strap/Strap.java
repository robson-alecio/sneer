package sneer.strap;

import static sneer.strap.SneerDirectories.findNewestMainApp;
import static sneer.strap.SneerDirectories.logDirectory;
import static sneer.strap.SneerDirectories.sneerDirectory;

import static sneer.strap.TestMode.createUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import wheelexperiments.Log;
import wheelexperiments.environment.ui.User;

public class Strap {

	private static final User _user = createUser();

	private static File _mainApp;
	
	
	public Strap() {
		try {
			tryToRun();
		} catch (Throwable t) {
			Log.log(t);
		}
	}

	
	private static void tryToRun() throws Exception {
		if (!sneerDirectory().exists()) tryToInstall();
		if (!sneerDirectory().exists()) return;

		tryToRedirectLogToSneerLogFile();

		if (!hasMainApp()) new VersionUpdateAttempt(1);
		if (!hasMainApp()) return;
		
		prepareSuccessor();
	}


	private static void tryToInstall() {
		new InstallationDialog(_user);
		sneerDirectory().mkdir();
	}


	private static void prepareSuccessor() throws Exception {
		System.setProperty("sneer.sucessor.JarPath", mainApp().toURI().toURL().toString());
		System.setProperty("sneer.sucessor.MainClass", "sneer.Sneer");
	}


	private static boolean hasMainApp() {
		return mainApp() != null;
	}

	
	protected static File mainApp() {
		if (_mainApp == null) _mainApp = findNewestMainApp();
		return _mainApp;
	}


	static void tryToRedirectLogToSneerLogFile() throws FileNotFoundException {
		logDirectory().mkdir();
		Log.redirectTo(new File(logDirectory(), "log.txt"));
	}

}
