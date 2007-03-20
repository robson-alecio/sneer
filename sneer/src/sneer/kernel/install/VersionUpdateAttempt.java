package sneer.kernel.install;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

import sneer.kernel.server.Command;
import sneer.kernel.server.ServerConfig;
import wheel.io.Log;
import wheel.io.ui.User;

public class VersionUpdateAttempt {

	private static Socket _socket;
	private static ObjectInputStream _objectIn;
	public static final String UP_TO_DATE = "UP TO DATE";


	
	public VersionUpdateAttempt(int nextVersion, User user) throws IOException {
		_nextVersion = nextVersion;
		_user = user;
		tryToDownloadMainApp();
	}

	
	private final int _nextVersion;
	private final User _user;

	
	private void tryToDownloadMainApp() throws IOException {
		try {
			openDownloadConnectionForVersion(_nextVersion);
			Log.log("Servidor encontrado...");
			
			((Command)receiveObject()).execute();

		} catch (ConnectException x) {
			Log.log("Servidor não enconrado.");
			
		} finally {
			closeDownloadConnection();
		}

	}

	
	private void openDownloadConnectionForVersion(int version) throws IOException {
		_user.acnowledgeNotification("Procurando atualização...");
		
		_socket = new Socket(ServerConfig.HOST, ServerConfig.PORT);
		
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
