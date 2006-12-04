package sneer.server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Date;

import sneer.boot.Boot;

public class VersionUpdateAgent implements Agent {
		
		private void upload(File file) throws IOException {
			send(version(file));
			send(contents(file));
		}

		
		private void send(Object toSend) throws IOException {
			_objectOut.writeObject(toSend);
		}

	private transient ObjectOutputStream _objectOut;

	private final int _requestedVersion;

	public VersionUpdateAgent(int requestedVersion) {
		_requestedVersion = requestedVersion;
	}


	private static int version(File mainApp) {
		return Boot.validNumber(mainApp.getName());
	}

	
	private static File newestMainApp() {
		return Boot.findNewestMainApp(new File("c:\\sneer\\mainapps"));
	}

	
	private static byte[] contents(File mainApp) throws IOException {
		DataInputStream dataIn = new DataInputStream(new FileInputStream(mainApp));
		byte[] result = new byte[dataIn.available()];
		dataIn.readFully(result);
		return result;
	}


	public void helpYourself(ObjectInputStream ignored, ObjectOutputStream objectOut) throws Exception {
		_objectOut = objectOut;

		File mainApp = newestMainApp();
		int newestVersion = Boot.validNumber(mainApp.getName());
		if (_requestedVersion > newestVersion) {
			log(Boot.UP_TO_DATE);
			send(Boot.UP_TO_DATE);
			return;
		}
		
		log("Uploading " + mainApp.getName() + "...");
		upload(mainApp);
		
		log("done.");
	}

}
