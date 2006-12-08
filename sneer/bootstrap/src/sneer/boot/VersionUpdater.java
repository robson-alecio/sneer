package sneer.boot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;

import javax.swing.JOptionPane;

public class VersionUpdater {

	private static File _mainApp;
	private static final String PREFIX = "main";
	private static final String ZERO_MASK = "000000";
	private static final String SUFFIX = ".jar";
	private static final int FILENAME_LENGTH = PREFIX.length() + ZERO_MASK.length() + SUFFIX.length();
	
	private static Socket _socket;
	private static ObjectInputStream _objectIn;
	public static final String UP_TO_DATE = "UP TO DATE";

	private static PrintWriter _log;

	
	public VersionUpdater(int nextVersion) {
		_nextVersion = nextVersion;
	}

	
	private final int _nextVersion;

	
	private static String zeroPad(int fileNumber) {
		String concat = ZERO_MASK + fileNumber;
		return concat.substring(concat.length() - ZERO_MASK.length());
	}



	protected static void tryToDownloadMainAppVersion(int version) throws IOException {
		int mainAppVersion;
		byte[] mainAppContents;
		try {
			openDownloadConnectionForVersion(version);
			Object received = receiveObject();
			if (UP_TO_DATE.equals(received)) {
				log("Servidor encontrado. Não há atualização nova para o Sneer.");
				return;
			}

			log("Servidor encontrado. Baixando atualização para o Sneer...");
			mainAppVersion = (Integer)received;
			mainAppContents = (byte[])receiveObject();
		} finally {
			closeDownloadConnection();
		}

		writeToMainAppFile(mainAppVersion, mainAppContents);
		log("Atualização baixada.");
	}

	
	private static void log(String entry) throws FileNotFoundException {
		int uncomment;
//		log().println("" + new Date() + "  " + entry);
//		log().println();
//		log().println();
//		log().flush();
	}


	private static void writeToMainAppFile(int version, byte[] contents) throws IOException {
		programsDirectory().mkdir();
		File part = new File(programsDirectory(), "sneer.part");
		FileOutputStream fos = new FileOutputStream(part);
		fos.write(contents);
		fos.close();
		
		part.renameTo(new File(programsDirectory(), PREFIX + zeroPad(version) + SUFFIX));
	}

	
	static private File logDirectory() {
		return new File(sneerDirectory(), "logs");
	}

	static private File programsDirectory() {
		return new File(sneerDirectory(), "programs");
	}
	
	
	protected static File sneerDirectory() {
		return new File(userHome(), ".sneer");
	}


	private static String userHome() {
		return System.getProperty("user.home");
	}

	
	private static void openDownloadConnectionForVersion(int version) throws IOException {
		_socket = new Socket("sovereigncomputing.net", 4242);
		
		new ObjectOutputStream(_socket.getOutputStream()).writeObject(version);
		
		_objectIn = new ObjectInputStream(_socket.getInputStream());
	}

	
	private static void closeDownloadConnection() {
		try {
			if (_socket != null) _socket.close();
		} catch (IOException ignored) {}

		_objectIn = null;
		_socket = null;
	}
	
	
	static private Object receiveObject() throws IOException {
		try {
			return _objectIn.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	
}
