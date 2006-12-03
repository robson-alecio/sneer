package sneer;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class BootstrapServer {

	private static final int PORT = 4242;

	static class Connection extends Thread {
		
		private final Socket _socket;
		private ObjectOutputStream _objectOut;

		
		Connection(Socket socket) {
			_socket = socket;
			setDaemon(true);
			start();
		}
	
		
		@Override
		public void run() {
			try {
				tryToServeSocket();
			} catch (Exception e) {
				e.printStackTrace();
				e.printStackTrace(_log);
			} finally {
				closeSocket();
			}
		}

		
		private void closeSocket() {
			try {
				_socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				e.printStackTrace(_log);
			}
		}

		
		private void tryToServeSocket() throws Exception {
			log("Connection received from " + _socket.getRemoteSocketAddress());
			
			File mainApp = newestMainApp();
			int newestVersion = Bootstrap2.validNumber(mainApp.getName());
			if (requestedVersion() > newestVersion) {
				log(Bootstrap2.UP_TO_DATE);
				send(Bootstrap2.UP_TO_DATE);
				return;
			}
				
			log("Uploading " + mainApp.getName() + "...");
			upload(mainApp);
			
			log("done.");
		}

		
		private void upload(File file) throws IOException {
			send(version(file));
			send(contents(file));
		}

		
		private void send(Object toSend) throws IOException {
			if (_objectOut == null)
				_objectOut = new ObjectOutputStream(_socket.getOutputStream());
			_objectOut.writeObject(toSend);
		}


		private int requestedVersion() throws Exception {
			InputStream inputStream = _socket.getInputStream();
			ObjectInputStream objectIn = new ObjectInputStream(inputStream);
			Object received = objectIn.readObject();
			if (Bootstrap.GREETING.equals(received)) return 1;
			return (Integer)received;
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
		_log = Bootstrap2.printWriterFor(new File("c:\\sneer\\serverlog.txt"));
	}


	private static void log(String string) {
		String entry = "" + new Date() + "   " + string;
		System.out.println(entry);
		_log.println(entry);
		_log.flush();
	}

	
	private static int version(File mainApp) {
		return Bootstrap2.validNumber(mainApp.getName());
	}

	
	private static File newestMainApp() {
		return Bootstrap2.findNewestMainApp(new File("c:\\sneer\\mainapps"));
	}

	
	private static byte[] contents(File mainApp) throws IOException {
		DataInputStream dataIn = new DataInputStream(new FileInputStream(mainApp));
		byte[] result = new byte[dataIn.available()];
		dataIn.readFully(result);
		return result;
	}

}
