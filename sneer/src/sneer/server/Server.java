package sneer.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import sneer.boot.Boot;
import sneer.boot.Bootstrap;
import wheelexperiments.Cool;

public class Server {

	private static final int PORT = 4242;

	private static PrintWriter _log;
	
	
	public static void main(String[] args) throws IOException {
		initLog();

		ServerSocket serverSocket = new ServerSocket(PORT);
		log("Waiting for connections on port " + PORT + "...");
		while (true) Cool.startDaemon(new Connection(serverSocket.accept()));
	}


	private static void initLog() throws FileNotFoundException {
		int uncomment;
		//_log = Boot.printWriterFor(new File("c:\\sneer\\serverlog.txt"));
	}


	public static void log(String string) {
		String entry = "" + new Date() + "   " + string;
		System.out.println(entry);
		_log.println(entry);
		_log.flush();
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
				e.printStackTrace(_log);
			} finally {
				closeSocket();
			}
		}

		
		private void closeSocket() {
			try { _socket.close(); } catch (IOException ignored) {}
		}

		
		private void tryToServeSocket() throws Exception {
			log("Connection received from " + _socket.getRemoteSocketAddress());
			
			ObjectInputStream objectIn = new ObjectInputStream(_socket.getInputStream());
			ObjectOutputStream objectOut = new ObjectOutputStream(_socket.getOutputStream());

			receivePeerRep(objectIn).helpYourself(objectIn, objectOut);
		}


		private Agent receivePeerRep(ObjectInputStream objectIn) throws IOException, ClassNotFoundException {
			Object candidate = objectIn.readObject();
			
			return candidate instanceof Agent
				? (Agent)candidate
				: backwardCompatiblePeerRep(candidate);
		}


		private Agent backwardCompatiblePeerRep(Object greeting) {
			int requestedVersion =	Bootstrap.GREETING.equals(greeting)
				? 1
				: (Integer)greeting;
			
			return new VersionUpdateAgent(requestedVersion);
		}

	}

}
