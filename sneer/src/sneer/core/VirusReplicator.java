/**
 * 
 */
package sneer.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

final class VirusReplicator implements Runnable {
	public void run() 	{
		try {
			replicate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void replicate() throws IOException {
		ServerSocket server = new ServerSocket(42);
		try {
			while (true) {		
				Socket socket = server.accept();
				System.out.println("Virus replication sequence started...");
				SovereignVirus.writeJar(socket);
				System.out.println("Virus successfully replicated.");
			}
		} finally {
			server.close();
		}
	}
}