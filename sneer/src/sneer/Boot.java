package sneer;

import javax.swing.JOptionPane;


public class Boot {
	
	public static void main(String[] ignored) {
		try {
			tryToRun();
		} catch (Throwable t) {
			show(t);
		}
	}


	private static void tryToRun() throws Exception {
		checkJavaVersion6OtherwiseExit();
		startSneerJockey();
	}

	private static void startSneerJockey() throws Exception {
		//Boot is compiled to run on Java 1.1 to show the "You need Java6 message", so it cannot propagate compilation dependencies.
		Boot.class.getClassLoader().loadClass("sneer.SneerJockey").newInstance();
	}


	private static void checkJavaVersion6OtherwiseExit() {
		String version = System.getProperty("java.specification.version");
		if (Float.parseFloat(version) >= 1.6f) return;
		
		String message = "You are running Sneer on Java version " + version + ".\n\n" +
				" You need Java version 6 or newer.";
		showError(message);
		System.exit(-1);
	}


	private static void show(Throwable t) {
		t.printStackTrace();
		showError(t.toString());
	}

	
	private static void showError(String message) {
		try {
			JOptionPane.showOptionDialog(null, " " + message + "\n\n", "Sneer", JOptionPane.ERROR_MESSAGE, 0, null, new Object[]{"Exit"}, "Exit");
		} catch (RuntimeException headlessExceptionDoesNotExistInOlderJREs) {
			System.out.println("ERROR: " + message);
		}
	}

}
