package sneer.installer;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class Main {

	public static void main(String[] args) throws Exception {
		if (!sneerHome().exists())
			new InstallationWizard(sneerHome());

		overcomeWebstartSecurityRestrictions();
		startSneer();
	}

	private static void startSneer() throws Exception {
		File binDirectory = new File(sneerHome(), "bin");
		URLClassLoader loader = new URLClassLoader(new URL[]{ binDirectory.toURI().toURL() });
		loader.loadClass("main.Sneer").newInstance();
	}
	
	private static File sneerHome() {
		return new File(userHome(), ".sneer");
	}
	
	private static String userHome() {
		String override = System.getProperty("home_override");
		if (override != null) return override;
		
		return System.getProperty("user.home");
	}

	private static void overcomeWebstartSecurityRestrictions() {
		System.setSecurityManager(new PermissiveSecurityManager());
	}

}