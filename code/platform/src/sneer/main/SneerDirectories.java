package sneer.main;

import java.io.File;

public class SneerDirectories {

	public static final File SNEER_HOME 	= sneerHome();
	public static final File DATA 			= new File(SNEER_HOME, dummyPrefix() + "data");
	public static final File LOG_FILE 		= new File(SNEER_HOME, dummyPrefix() + "logs/log.txt");
	public static final File CODE 			= new File(SNEER_HOME, "code");
	public static final File OWN_CODE 		= new File(SNEER_HOME, "code/own");
	public static final File OWN_BIN 		= new File(SNEER_HOME, "code/own/bin");
	public static final File PLATFORM_CODE 	= new File(SNEER_HOME, "code/platform");
	public static final File PLATFORM_BIN 	= new File(SNEER_HOME, "code/platform/bin");

	private static File sneerHome() {
		String override = System.getProperty("sneer.home");
		if (override != null) return new File(override);

		return new File(System.getProperty("user.home"), "sneer");
	}

	private static String dummyPrefix() {
		return "true".equals(System.getProperty("sneer.dummy"))
			? "dummy"
			: "";
	}

}
