package sneer.server;

import java.io.File;

public class ServerConfig {

	static public final String HOST = "sovereigncomputing.net";
	static public final int PORT = 22086;
	
	static public final File MAIN_APP_DIRECTORY = new File(sneerProjectDirectory(), "mainapps");

	private static File sneerProjectDirectory() {   //The server runs in .../sneer/bin
		return new File("whatever").getParentFile().getParentFile();  //Returns .../sneer
	}

}
