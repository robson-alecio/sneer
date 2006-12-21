package sneer.strap;

import static sneer.strap.SneerDirectories.findNewestMainApp;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import wheelexperiments.Log;
import wheelexperiments.environment.ui.User;

public class Main {

	private static final User _user = new User();

	private static File _mainApp;

	
	public static void main(String[] ignored) {
		try {
			tryToRun();
		} catch (Throwable t) {
			Log.log(t);
		}
	}

	private static ClassLoader vmBootstrapClassLoader() {
		ClassLoader candidate = ClassLoader.getSystemClassLoader();
		while (candidate.getParent() != null) candidate = candidate.getParent();
		return candidate;
	}

	private static void tryToRun() throws Exception {
		if (!hasMainApp()) new InstallationAttempt(_user);
		if (!hasMainApp()) return;
		
		runMainApp();
	}


	private static void runMainApp() throws Exception {
		InstallationAttempt.tryToRedirectLog();
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


}
