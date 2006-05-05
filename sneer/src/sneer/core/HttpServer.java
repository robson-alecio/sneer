/**
 * 
 */
package sneer.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

final class HttpServer implements Runnable {
	public void run() {
		try {
			handleClients();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleClients() throws IOException {
		ServerSocket server = new ServerSocket(8080);
		try {
			while (true) {		
				handleClient(server.accept());
			}
		} finally {
			server.close();
		}
	}

	private void handleClient(Socket socket) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));		
		String line = reader.readLine();
		String log = line;
		do {
			System.out.println(log);		 
			log = reader.readLine();
		} while (null != log && log.length() > 0);
		
		if (line.startsWith("GET /" + SovereignVirus.JAR_NAME)) {
			SovereignVirus.sendSacredLoader(socket);
		} else {
			SovereignVirus.welcome(socket);
		}
	}
}