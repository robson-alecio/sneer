package sneer.strap;

import static sneer.strap.SneerDirectories.findNewestMainApp;
import static sneer.strap.SneerDirectories.logDirectory;
import static sneer.strap.SneerDirectories.sneerDirectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import wheelexperiments.Log;
import wheelexperiments.environment.ui.User;

public class Strap {

	private static final User _user = new User();

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
		
		runMainApp();
	}


	private static void tryToInstall() {
		new InstallationDialog(_user);
		sneerDirectory().mkdir();
	}


	private static void runMainApp() throws Exception {
		invokeMainMethodOn(mainClass(mainApp()));
	}


	private static Class<?> mainClass(File mainApp) throws Exception {
		return new URLClassLoader(new URL[] { mainApp.toURI().toURL() }).loadClass("sneer.Main");
	}


	private static void invokeMainMethodOn(Class<?> clazz) throws Exception {
		clazz.getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { new String[0] });
	}

	
	private static boolean hasMainApp() {
		return mainApp() != null;
	}

	
	protected static File mainApp() {
		if (_mainApp == null) _mainApp = findNewestMainApp();
		return _mainApp;
	}

	private static ClassLoader vmBootstrapClassLoader() {
		ClassLoader candidate = ClassLoader.getSystemClassLoader();
		while (candidate.getParent() != null) candidate = candidate.getParent();
		return candidate;
	}


	static void tryToRedirectLogToSneerLogFile() throws FileNotFoundException {
		logDirectory().mkdir();
		Log.redirectTo(new File(logDirectory(), "log.txt"));
	}


}
