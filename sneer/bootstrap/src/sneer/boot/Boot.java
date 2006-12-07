package sneer.boot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JOptionPane;

import wheelexperiments.Log;

public class Boot {

	private static final User _user = new User();

	private static File _mainApp;
	private static final String PREFIX = "main";
	private static final String ZERO_MASK = "000000";
	private static final String SUFFIX = ".jar";
	private static final int FILENAME_LENGTH = PREFIX.length() + ZERO_MASK.length() + SUFFIX.length();

	
	public static void main(String[] ignored) {
		try {
			tryToRun();
		} catch (Throwable t) {
			logOtherwiseShow(t);
		}
	}


	private static void tryToRun() throws Exception {
		checkJavaVersionOtherwiseExit();
		
		if (!hasMainApp()) tryToInstallMainAppOtherwiseExit();
		runMainApp();
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
		if (_mainApp == null) _mainApp = findNewestMainApp(programsDirectory());
		return _mainApp;
	}

	
	public static File findNewestMainApp(File directory) {
		int newest = 0;
		for (String filename : listFilenames(directory))
			if (validNumber(filename) > newest) newest = validNumber(filename);  
		
		if (newest == 0) return null;
		return new File(directory, PREFIX + zeroPad(newest) + SUFFIX);
	}

	
	private static String[] listFilenames(File directory) {
		String[] result = directory.list();
		if (result == null) return new String[0];
		return result;
	}

	
	private static String zeroPad(int fileNumber) {
		String concat = ZERO_MASK + fileNumber;
		return concat.substring(concat.length() - ZERO_MASK.length());
	}

	
	public static int validNumber(String mainAppCandidate) {
		if (!mainAppCandidate.startsWith(PREFIX)) return -1;
		if (!mainAppCandidate.endsWith(SUFFIX)) return -1;
		if (mainAppCandidate.length() != FILENAME_LENGTH) return -1;
		
		try {
			return Integer.parseInt(mainAppCandidate.substring(PREFIX.length(), PREFIX.length() + ZERO_MASK.length()));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	
	private static void tryToInstallMainAppOtherwiseExit() throws IOException {
		if (!sneerDirectory().exists()) {
			int uncomment;
//			approveInstallationWithUserOtherwiseExit();
			tryToCreateSneerDirectory();
			createLog();
		}
		new VersionUpdater(1);
	}


	private static Log createLog() throws FileNotFoundException {
		logDirectory().mkdir();
		return new Log(new File(logDirectory(), "log.txt"));
	}

	
	private static void tryToCreateSneerDirectory() throws IOException {
		if (!sneerDirectory().mkdir())
			throw new IOException("Unable to create Sneer directory\n" + sneerDirectory());
	}


	static private File logDirectory() {
		return new File(sneerDirectory(), "logs");
	}

	
	static private File programsDirectory() {
		return new File(sneerDirectory(), "programs");
	}
	
	
	protected static File sneerDirectory() {
		return new File(_user.homeDirectory(), ".sneer");
	}

	
	private static void checkJavaVersionOtherwiseExit() {
		String version = System.getProperty("java.specification.version");
		if (Float.parseFloat(version) >= 1.6f) return;
		
		_user.exit("O Sneer precisa ser rodado com o Java versão 6 ou mais recente.");
	}


	private static void logOtherwiseShow(Throwable throwable) {
		Log log;
		try {
			log = createLog();
		} catch (FileNotFoundException ignored) {
			showFailureToLog(throwable);
			return;
		}
		
		log.log(throwable);
		
		if (log.hasError()) showFailureToLog(throwable);
	}
	
	
	private static void showFailureToLog(Throwable throwable) {
		throwable.printStackTrace();
		_user.acknowledgeUnexpectedProblem(throwable.toString() + "\n\n (Unable to write this Exception to log file)\n");
	}

}
