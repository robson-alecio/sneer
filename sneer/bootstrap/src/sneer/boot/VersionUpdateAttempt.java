package sneer.boot;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import sneer.server.Command;
import sneer.server.ServerAddress;
import wheelexperiments.Log;

public class VersionUpdateAttempt {

	private static Socket _socket;
	private static ObjectInputStream _objectIn;
	public static final String UP_TO_DATE = "UP TO DATE";


	
	public VersionUpdateAttempt(int nextVersion) throws IOException {
		_nextVersion = nextVersion;
		tryToDownloadMainApp();
	}

	
	private final int _nextVersion;

	
	private void tryToDownloadMainApp() throws IOException {
		try {
			openDownloadConnectionForVersion(_nextVersion);
			Log.log("Servidor encontrado...");
			
			((Command)receiveObject()).execute();

		} finally {
			closeDownloadConnection();
		}

	}

	
	private void openDownloadConnectionForVersion(int version) throws IOException {
		_socket = new Socket(ServerAddress.HOST, ServerAddress.PORT);
		
		ObjectOutputStream objectOut = new ObjectOutputStream(_socket.getOutputStream());
		objectOut.writeObject(new VersionUpdateAgent(version));
		
		_objectIn = new ObjectInputStream(_socket.getInputStream());
	}

	
	private static void closeDownloadConnection() {
		try {
			if (_socket != null) _socket.close();
		} catch (IOException ignored) {}

		_objectIn = null;
		_socket = null;
	}
	
	
	static private Object receiveObject() throws IOException {
		try {
			return _objectIn.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	
}
