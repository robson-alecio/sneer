package sneer;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class BootstrapServer {

	private static final int PORT = 4242;

	static class Connection extends Thread {
		private final Socket _socket;

		Connection(Socket socket) {
			_socket = socket;
			setDaemon(true);
			start();
		}
		
		@Override
		public void run() {
			try {
				tryToRun();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void tryToRun() throws Exception {
			log("Connection received from " + _socket.getRemoteSocketAddress());
			if (!validConnection()) {log("invalid");return;}
			File mainApp = newestMainApp();
			log("Uploading " + mainApp.getName() + "...");
			upload(mainApp);
			log("done");
		}

		private void upload(File file) throws IOException {
			OutputStream outputStream = _socket.getOutputStream();
			ObjectOutputStream objectOut = new ObjectOutputStream(outputStream);
			objectOut.writeObject(version(file));
			objectOut.writeObject(contents(file));
		}

		private boolean validConnection() throws Exception {
			InputStream inputStream = _socket.getInputStream();
			ObjectInputStream objectIn = new ObjectInputStream(inputStream);
			return Bootstrap.GREETING.equals(objectIn.readObject());
		}
	}

	private static PrintWriter _log;

	public static void main(String[] args) throws IOException {
		initLog();

		ServerSocket serverSocket = new ServerSocket(PORT);
		log("Waiting for connections on port " + PORT + "...");
		while (true) new Connection(serverSocket.accept());
	}


	private static void initLog() throws FileNotFoundException {
		_log = new PrintWriter(new FileOutputStream(new File("c:\\sneer\\serverlog.txt"), true));
	}


	private static void log(String string) {
		String entry = "" + new Date() + "   " + string;
		System.out.println(entry);
		_log.println(entry);
		_log.flush();
	}

	private static int version(File mainApp) {
		return Bootstrap.validNumber(mainApp.getName());
	}

	private static File newestMainApp() {
		return Bootstrap.findNewestMainApp(new File("c:\\sneer\\mainapps"));
	}

	private static byte[] contents(File mainApp) throws IOException {
		DataInputStream dataIn = new DataInputStream(new FileInputStream(mainApp));
		byte[] result = new byte[dataIn.available()];
		dataIn.readFully(result);
		return result;
	}

}
