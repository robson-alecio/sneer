package sneer.server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import wheelexperiments.Cool;
import wheelexperiments.Log;

public class Server {

	static public final int PORT = 4242;

	private static Log _log;
	
	
	public static void main(String[] args) throws IOException {
		initLog();

		ServerSocket serverSocket = new ServerSocket(PORT);
		_log.log("Waiting for connections on port " + PORT + "...");
		while (true) Cool.startDaemon(new Connection(serverSocket.accept()));
	}


	private static void initLog() {
		_log = new Log(new File("c:\\sneer\\serverlog.txt"));
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
				e.printStackTrace();
				_log.log(e);
			} finally {
				closeSocket();
			}
		}

		
		private void closeSocket() {
			try { _socket.close(); } catch (IOException ignored) {}
		}

		
		private void tryToServeSocket() throws Exception {
			_log.log("Connection received from " + _socket.getRemoteSocketAddress());
			
			ObjectInputStream objectIn = new ObjectInputStream(_socket.getInputStream());
			ObjectOutputStream objectOut = new ObjectOutputStream(_socket.getOutputStream());

			((Agent)objectIn.readObject()).helpYourself(objectIn, objectOut);
		}

	}

}
