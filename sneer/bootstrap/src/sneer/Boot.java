package sneer;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.swing.JOptionPane;

public class Boot extends ClassLoader {
	
	private static final String TITLE = "Sneer Friend-to-Friend Installation";
	
	private Socket _socket;
	private ObjectInputStream _objectIn;

	protected String _host = null;
	protected int _port = 0;

	private String _address = "Ask your friend what to type in here.";

	protected byte[] _strapCode; //Not private for testing purposes.

	
	public static void main(String[] ignored) {
		new Boot().run();
	}

	private void run() {
		try {
			tryToRun();
		} catch (Throwable t) {			
			showError(t);
		}
	}

	private void tryToRun() throws Exception {
		while (!isMainAppInstalled())
			tryToInstallMainAppFromPeer("< Back");
		executeMainApp();
	}

	private boolean isMainAppInstalled() {
		return mainJar().exists();
	}

	protected File mainJar() {
		return new File(programsDirectory(), "Main.jar");
	}


	private void tryToInstallMainAppFromPeer(String remedy) throws Exception {
		try{
			downloadStrapFromPeer();
			closeConnectionToPeer();
			runStrap();
		} catch (Exception x) {
			int option = JOptionPane.showOptionDialog(null, interpret(x), "Error", JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{remedy, "Close"}, remedy);
			if (option != 0) System.exit(0);
			closeConnectionToPeer();
		}
	}


	protected void runStrap() throws Exception {  //Not private for testing purposes.
		authenticateStrapCode();

        Class<?> clazz = defineClass("sneer.Strap", _strapCode, 0, _strapCode.length);
        resolveClass(clazz);
		clazz.getMethod("run", new Class[] {String.class, Integer.TYPE}).invoke(null, new Object[] {_host, _port});
	}

	private String interpret(Exception x) {
		return x.getClass().getSimpleName().replaceAll("Exception", " ") + x.getMessage();
	}

	private void showMessage(String message, int type, String okButton) {
		JOptionPane.showOptionDialog(null, message, TITLE, 0, type, null, new Object[]{okButton}, okButton);
	}

	
	private void authenticateStrapCode() throws Exception {
		MessageDigest digester = MessageDigest.getInstance("SHA-512", "SUN");
		byte[] hash = digester.digest(_strapCode);
		
		if (!Arrays.equals(hash, expectedHash())) throw new Exception("Authentication failed. Your friend might be running a tampered version of Sneer.");
	}

	
	protected byte[] expectedHash() {  //Not private for testing purposes.
		return new byte[]{1, 2, 3};
	}

	
	private File programsDirectory() {
		return new File(sneerDirectory(), "programs");
	}

	private void closeConnectionToPeer() throws IOException {
		if (_objectIn != null) _objectIn.close();
		if (_socket != null) _socket.close();

		_objectIn = null;
		_socket = null;
	}

	protected byte[] receiveByteArray() throws Exception {
		return (byte[])_objectIn.readObject();
	}

	private File sneerDirectory() {
		return new File(System.getProperty("user.home"), ".sneer");
	}

	private void downloadStrapFromPeer() throws Exception {
		_address = promptForHostnameAndPort();
		parseAddress();

		openConnectionToPeer("Boot");

		_strapCode = receiveByteArray();
	}

	protected void openConnectionToPeer(String greeting) throws UnknownHostException, IOException {
		_socket = new Socket(_host, _port);
		_objectIn = new ObjectInputStream(_socket.getInputStream());
		
		ObjectOutputStream output = new ObjectOutputStream(_socket.getOutputStream());
		output.writeObject(greeting);
	}


	private void executeMainApp() throws Exception {
		Class<?> clazz = loadClass("sneer.Main");
		String[] args = {};
		clazz.getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { args });
	}

	private String promptForHostnameAndPort() throws Exception {  //Not private for testing purposes.
		String message =
			" Welcome.  :)\n\n" +
			" You will need an expert sovereign friend to guide\n" +
			" your first steps in sovereign computing.\n\n" +
			" Ask your friend what to type here:";
		String result = (String)JOptionPane.showInputDialog(null, message, TITLE, JOptionPane.PLAIN_MESSAGE, null, null, null);
		if (result == null) System.exit(0);
		
		return result;
	}

	private void parseAddress() throws Exception {
		try {
			tryToParseAddress();
		} catch (RuntimeException e) {
			throw new Exception("What you type must be in hostaddress:portnumber format.");
		}
	}

	private void tryToParseAddress() {
		String[] parts = _address.split(":");
		_host = parts[0];
		_port = Integer.parseInt(parts[1]);
	}

	private void showError(Throwable t) {
		String message = "There was an unexpected error:\n" +
			t + "\n\n" +
			"Sneer will now close.";
		showError(message);
	}

	protected void showError(String message) {
		showMessage(message, JOptionPane.ERROR_MESSAGE, "Whatever");
	}
}