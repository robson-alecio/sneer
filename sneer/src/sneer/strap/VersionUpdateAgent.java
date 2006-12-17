package sneer.strap;

import static sneer.strap.SneerDirectories.findNewestMainApp;
import static sneer.strap.SneerDirectories.validNumber;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import sneer.server.Agent;
import sneer.server.Command;
import sneer.server.ServerConfig;
import wheelexperiments.Log;

public class VersionUpdateAgent implements Agent {

	public VersionUpdateAgent(int requestedVersion) {
		_requestedVersion = requestedVersion;
	}

	
	private final int _requestedVersion;
	
	private transient ObjectOutputStream _objectOut;
	
		
	public void helpYourself(ObjectInputStream ignored, ObjectOutputStream objectOut) throws Exception {
		_objectOut = objectOut;
		send(commandToSend());
		Log.log("Done.");
	}

	
	private Command commandToSend() throws IOException {
		File mainApp = newestMainApp();
		if (mainApp == null) {
			Log.log("No mainApp files found.");
			return noNewVersionCommand();
		}
		
		int newestVersion = validNumber(mainApp.getName());
		if (_requestedVersion > newestVersion) {
			Log.log("Up to date. Version " + newestVersion);
			return noNewVersionCommand();
		}
		
		Log.log("Uploading " + mainApp.getName() + "...");
		return new VersionUpdateCommand(version(mainApp), contents(mainApp));
	}


	private LogMessage noNewVersionCommand() {
		return new LogMessage("Não há atualização nova para o Sneer.");
	}

		
	private void send(Object toSend) throws IOException {
		_objectOut.writeObject(toSend);
		_objectOut.flush();
	}


	private static int version(File mainApp) {
		return validNumber(mainApp.getName());
	}

	
	private static File newestMainApp() {
		return findNewestMainApp(ServerConfig.MAIN_APP_DIRECTORY);
	}

	
	private static byte[] contents(File mainApp) throws IOException {
		DataInputStream dataIn = new DataInputStream(new FileInputStream(mainApp));
		byte[] result = new byte[dataIn.available()];
		dataIn.readFully(result);
		return result;
	}

	private static final long serialVersionUID = 1L;

}
