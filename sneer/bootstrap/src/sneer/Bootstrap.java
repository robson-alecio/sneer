package sneer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JOptionPane;

public class Bootstrap {

	private static File _mainApp;
	private static final String PREFIX = "main";
	private static final String ZERO_MASK = "000000";
	private static final String SUFFIX = ".jar";
	private static final int FILENAME_LENGTH = PREFIX.length() + ZERO_MASK.length() + SUFFIX.length();
	
	private static Socket _socket;
	private static ObjectInputStream _objectIn;
	public static final String GREETING = "Sneer Bootstrap";
	

	public static void main(String[] ignored) {
			try {
				tryToRun();
			} catch (Throwable t) {
				t.printStackTrace();
				JOptionPane.showMessageDialog(null, t.toString(), "Sneer - Unexpected Problem", JOptionPane.ERROR_MESSAGE);
			}
	}

	
	private static void tryToRun() throws Exception {
		if (!hasMainApp()) downloadMainApp();
		runMainApp();
	}

	
	private static void runMainApp() throws Exception {
		Class<?> clazz = new URLClassLoader(new URL[] { mainApp().toURI().toURL() }).loadClass("sneer.Main");
		clazz.getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { new String[0] });
	}

	
	private static boolean hasMainApp() {
		return mainApp() != null;
	}

	
	private static File mainApp() {
		if (_mainApp == null) _mainApp = findNewestMainApp();
		return _mainApp;
	}

	
	private static File findNewestMainApp() {
		int newest = 0;
		for (String filename : listFilenames(programsDirectory()))
			if (validNumber(filename) > newest) newest = validNumber(filename);  
		
		if (newest == 0) return null;
		return new File(PREFIX + zeroPad(newest) + SUFFIX);
	}

	
	private static String[] listFilenames(File directory) {
		String[] result = directory.list();
		if (result == null) return new String[0];
		return result;
	}

	
	private static String zeroPad(int fileNumber) {
		String concat = ZERO_MASK + fileNumber;
		return concat.substring(concat.length() - ZERO_MASK.length());
	}

	
	private static int validNumber(String mainAppCandidate) {
		if (!mainAppCandidate.startsWith(PREFIX)) return -1;
		if (!mainAppCandidate.endsWith(SUFFIX)) return -1;
		if (mainAppCandidate.length() != FILENAME_LENGTH) return -1;
		
		try {
			return Integer.parseInt(mainAppCandidate.substring(PREFIX.length(), PREFIX.length() + ZERO_MASK.length()));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	
	private static void downloadMainApp() {
		while (true) {
			try {
				tryToDownloadMainApp();
				return;
			} catch (IOException e) {System.out.println("" + System.currentTimeMillis() + " - " + e.getMessage()); e.printStackTrace();}

			int oneHour = 1000 * 60 * 60;
			sleep(oneHour);
		}
	}

	
	private static void tryToDownloadMainApp() throws IOException {
		byte[] jarContents = downloadMainAppJarContents();
		writeToMainAppFile(jarContents);
	}

	
	private static void writeToMainAppFile(byte[] jarContents) throws IOException {
		programsDirectory().mkdirs();
		File part = new File(programsDirectory(), "sneer.part");
		FileOutputStream fos = new FileOutputStream(part);
		fos.write(jarContents);
		fos.close();
		
		part.renameTo(new File(programsDirectory(), PREFIX + zeroPad(1) + SUFFIX));
	}

	
	static private File programsDirectory() {
		return new File(sneerDirectory(), "programs");
	}
	
	
	static private File sneerDirectory() {
		return new File(System.getProperty("user.home"), ".sneer");
	}

	
	private static byte[] downloadMainAppJarContents() throws IOException {
		try {
			openDownloadConnection();
			return receiveByteArray();
		} finally {
			closeDownloadConnection();
		}
	}

	
	private static void openDownloadConnection() throws IOException {
		_socket = new Socket("klaus.selfip.net", 4242);
		
		new ObjectOutputStream(_socket.getOutputStream()).writeObject(GREETING);
		
		_objectIn = new ObjectInputStream(_socket.getInputStream());
	}

	
	private static void closeDownloadConnection() {
		try {
			if (_socket != null) _socket.close();
		} catch (IOException ignored) {}

		_objectIn = null;
		_socket = null;
	}
	
	
	static private byte[] receiveByteArray() throws IOException {
		try {
			return (byte[])_objectIn.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	
	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {}
	}
	
}
