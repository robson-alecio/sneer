package sneer.platform.server;

import java.io.File;

import sneer.platform.TestMode;

public class ServerConfig {

	static public final String HOST = host();
	static public final int PORT = 22086;
	
	static public final File MAIN_APP_DIRECTORY = new File("mainapps");

	private static String host() {
		return TestMode.isInTestMode()
			? "localhost"
			: "sovereigncomputing.net";
	}

}
