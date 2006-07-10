package sneer.core;



import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

final public class VirusReplicator implements Runnable {
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
				VirusReplicator.writeJar(socket);
				System.out.println("Virus successfully replicated.");
			}
		} finally {
			server.close();
		}
	}

	static File mainAppJarFile() {
		String path =
			System.getProperty("user.home") + File.separator +
			".sneer" + File.separator +
			"application" + File.separator +
			"MainApplication.zip";
		return new File(path);
	}

	public static void initialize() throws IOException {		
		startDaemon(new VirusReplicator());
		System.out.println("Press certain keys to quit");
		System.in.read();
	}

	private static void startDaemon(Runnable closure) {
		Thread thread = new Thread(closure);
		thread.setDaemon(true);
		thread.start();
	}

	private static void writeObject(Socket socket, Object o) throws IOException {
		OutputStream sos = socket.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(sos);		
		oos.writeObject(o);
		oos.flush();
	}

	static void writeJar(Socket socket) throws IOException {		
		File zip = mainAppJarFile();
		DataInputStream di = new DataInputStream(new FileInputStream(zip));
		byte tmp[] =  new byte[di.available()];
		di.readFully(tmp);
		writeObject(socket, tmp);
	}
}