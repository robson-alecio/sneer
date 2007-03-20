package sneer.kernel.server;

import static sneer.kernel.server.ServerConfig.PORT;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import wheel.io.Log;
import wheel.lang.Threads;

public class Server {

	public static void main(String[] args) throws IOException {
		initLog();

		ServerSocket serverSocket = new ServerSocket(PORT);
		Log.log("Waiting for connections on port " + PORT + "...");
		while (true) Threads.startDaemon(new Connection(serverSocket.accept()));
	}


	private static void initLog() throws FileNotFoundException {
		Log.redirectTo(new File("serverlog.txt"));
	}


	static class Connection implements Runnable {
		
		private final Socket _socket;

		
		Connection(Socket socket) {
			_socket = socket;
		}
	
		
		public void run() {
			try {
				tryToServeSocket();
			} catch (Exception e) {
				Log.log(e);
			} finally {
				closeSocket();
			}
		}

		
		private void closeSocket() {
			try { _socket.close(); } catch (IOException ignored) {}
		}

		
		private void tryToServeSocket() throws Exception {
			Log.log("Connection received from " + _socket.getRemoteSocketAddress());
			
			ObjectInputStream objectIn = new ObjectInputStream(_socket.getInputStream());
			ObjectOutputStream objectOut = new ObjectOutputStream(_socket.getOutputStream());

			((Agent)objectIn.readObject()).helpYourself(objectIn, objectOut);
		}

	}

}
