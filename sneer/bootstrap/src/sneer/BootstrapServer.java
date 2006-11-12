package sneer;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BootstrapServer {

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
			System.out.println("Connection received...");
			OutputStream outputStream = _socket.getOutputStream();    // Get the OUTPUT stream first. JDK 1.3.1_01 for Windows will lock up if you get the INPUT stream first.

			if (!validConnection()) {System.out.println("invalid");return;}
			
			ObjectOutputStream objectOut = new ObjectOutputStream(outputStream);
			System.out.print("writing...");
			objectOut.write(jarContents());
			System.out.println("done");
		}

		private byte[] jarContents() throws IOException {
			DataInputStream dataIn = new DataInputStream(new FileInputStream("c:\\temp\\sneerToBeServed.jar"));
			byte[] result = new byte[dataIn.available()];
			dataIn.readFully(result);
			return result;
		}

		private boolean validConnection() throws Exception {
			InputStream inputStream = _socket.getInputStream();
			ObjectInputStream objectIn = new ObjectInputStream(inputStream);
			return Bootstrap.MAIN_JAR_REQUEST.equals(objectIn.readObject());
		}
	}

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(4242);
		System.out.println("Waiting...");
		while (true) new Connection(serverSocket.accept());
	}

}
