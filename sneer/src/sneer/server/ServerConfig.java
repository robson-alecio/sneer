package sneer.server;

import java.io.File;
import java.io.IOException;

public class ServerConfig {

	static public final String HOST = "sovereigncomputing.net";
	static public final int PORT = 22086;
	
	static public final File MAIN_APP_DIRECTORY = mainAppDirectory();

	private static File mainAppDirectory() {
		try {
			return new File("../canmainapps").getCanonicalFile();
		} catch (IOException e) {
			return new File("../mainapps2");
		}
	}

}
