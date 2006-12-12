package sneer.server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import sneer.boot.VersionUpdateCommand;
import wheelexperiments.Log;

import static sneer.boot.SneerDirectories.*;

public class VersionUpdateAgent implements Agent {

	public VersionUpdateAgent(int requestedVersion) {
		_requestedVersion = requestedVersion;
	}

	
	private final int _requestedVersion;
	
	private transient ObjectOutputStream _objectOut;
	
		
	public void helpYourself(ObjectInputStream ignored, ObjectOutputStream objectOut) throws Exception {
		_objectOut = objectOut;
		
		File mainApp = newestMainApp();
		int newestVersion = validNumber(mainApp.getName());
		if (_requestedVersion > newestVersion) {
			Log.log("Up to date.");
			send(new LogMessage("Não há atualização nova para o Sneer."));
			return;
		}
		
		Log.log("Uploading " + mainApp.getName() + "...");
		upload(mainApp);
		
		Log.log("done.");
	}

	private void upload(File file) throws IOException {
		send(new VersionUpdateCommand(version(file), contents(file)));
	}
		
	private void send(Object toSend) throws IOException {
		_objectOut.writeObject(toSend);
	}


	private static int version(File mainApp) {
		return validNumber(mainApp.getName());
	}

	
	private static File newestMainApp() {
		return findNewestMainApp(new File("c:\\sneer\\mainapps"));
	}

	
	private static byte[] contents(File mainApp) throws IOException {
		DataInputStream dataIn = new DataInputStream(new FileInputStream(mainApp));
		byte[] result = new byte[dataIn.available()];
		dataIn.readFully(result);
		return result;
	}

	private static final long serialVersionUID = 1L;

}
