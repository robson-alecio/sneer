package sneer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Bootstrap {

	private static final String JAR = ".jar";
	private static final String SNEER = "sneer";
	private static final String ZEROS = "000000";
	private static final int FILENAME_LENGTH = SNEER.length() + ZEROS.length() + JAR.length();
	private static ObjectInputStream _objectIn;
	private static Socket _socket;

	public static void main(String[] ignored) {
			try {
				tryToRun();
			} catch (Throwable t) {			
				JOptionPane.showMessageDialog(null, t.toString(), "Sneer - Unexpected Problem", JOptionPane.ERROR_MESSAGE);
			}
	}

	private static void tryToRun() {
		if (!hasMainApp()) downloadMainApp();
		runMainApp();
	}

	private static void runMainApp() {
		String continueCodingFromHere;
	}

	private static boolean hasMainApp() {
		return mainApp() != null;
	}

	private static File mainApp() {
		int lastNumber = 0;

		for (String filename : programsDirectory().list())
			if (validNumber(filename) > lastNumber) lastNumber = validNumber(filename);  
		
		if (lastNumber == 0) return null;
		return new File(SNEER + zeroPad(lastNumber) + JAR);
	}



	private static String zeroPad(int fileNumber) {
		String concat = ZEROS + fileNumber;
		return concat.substring(concat.length() - ZEROS.length());
	}

	private static int validNumber(String mainJar) {
		if (!mainJar.startsWith(SNEER)) return -1;
		if (!mainJar.endsWith(JAR)) return -1;
		if (mainJar.length() != FILENAME_LENGTH) return -1;
		
		try {
			return Integer.parseInt(mainJar.substring(5, 11));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private static void downloadMainApp() {
		while (true) {
			try {
				tryToDownloadMainApp();
				return;
			} catch (IOException e) {}

			int oneHour = 1000 * 60 * 60;
			sleep(oneHour);
		}
	}

	private static void sleep(int oneHour) {
		try {
			Thread.sleep(oneHour);
		} catch (InterruptedException e) {}
	}

	private static void tryToDownloadMainApp() throws IOException {
		byte[] jarContents = getMainAppJarContents();
		writeToMainAppFile(jarContents);
	}

	private static void writeToMainAppFile(byte[] jarContents) throws IOException {
		File part = new File(programsDirectory(), "Sneer.part");
		FileOutputStream fos = new FileOutputStream(part);
		fos.write(jarContents);
		
		part.renameTo(new File(programsDirectory(), SNEER + zeroPad(1) + JAR));
	}

	private static byte[] getMainAppJarContents() throws IOException {
		try {
			openDownloadConnection();
			return getMainAppJarContentsFromConnection();
		} finally {
			closeDownloadConnection();
		}
	}


	
	private static byte[] getMainAppJarContentsFromConnection() throws IOException {
		ObjectOutputStream objectOut = new ObjectOutputStream(_socket.getOutputStream());
		objectOut.writeObject("Please send Sneer jar");
		objectOut.close();

		return receiveByteArray();
	}

	static private File programsDirectory() {
		return new File(sneerDirectory(), "programs");
	}
	
	static private File sneerDirectory() {
		return new File(System.getProperty("user.home"), ".sneer");
	}

	private static void closeDownloadConnection() throws IOException {
		if (_objectIn != null) _objectIn.close();
		if (_socket != null) _socket.close();

		_objectIn = null;
		_socket = null;
	}

	private static void openDownloadConnection() throws IOException {
		_socket = new Socket("klaus.selfip.net", 4242);
		_objectIn = new ObjectInputStream(_socket.getInputStream());
	}

	static private byte[] receiveByteArray() throws IOException {
		try {
			return (byte[])_objectIn.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	
	
}
