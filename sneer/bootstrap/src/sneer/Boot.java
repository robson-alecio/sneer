package sneer;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;

public class Boot extends ClassLoader {
	
	private static final String TITLE = "Sneer Friend-to-Friend Installation";
	
	private Socket _socket;
	private ObjectInputStream _objectIn;

	private String _host = null;
	private int _port = 0;

	private String _address = "Ask your friend what to type in here.";

	private byte[] _strapCode;

	
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
		return mainAppFile() != null;
	}

	private File mainAppFile() {
		return null;
	}


	private void tryToInstallMainAppFromPeer(String remedy) throws Exception, IOException {
		try{
			_address = promptForHostnameAndPort();
			parseAddress();
			openConnectionToPeer();
			runStrapFromPeer();
		} catch (Exception x) {
			int option = JOptionPane.showOptionDialog(null, interpret(x), "Error", JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{remedy, "Close"}, remedy);
			if (option != 0) System.exit(0);
		} finally {
			closeConnectionToPeer();
		}
	}


	protected void runStrapFromPeer() throws Exception {  //Not private for testing purposes.
			_strapCode = receiveByteArray();
			authenticateStrapCode();
	        Class<?> clazz = defineClass(strapClassName(), _strapCode, 0, _strapCode.length);
	        resolveClass(clazz);
			clazz.getMethod("run", new Class[] {}).invoke(null, new Object[] {});
	}

	protected String strapClassName() { //Not private for testing purposes.
		return Strap.class.getName();
	}

	private String interpret(Exception x) {
		return x.getClass().getSimpleName().replaceAll("Exception", " ") + x.getMessage();
	}

	private void showMessage(String message, int type, String okButton) {
		JOptionPane.showOptionDialog(null, message, TITLE, 0, type, null, new Object[]{okButton}, okButton);
	}

	
	private void execute(File jar, String... args) throws Exception {
		executeClass(jar, mainClass(jar), args);
	}

	
	private String mainClass(File jar) throws IOException {
		return new JarFile(jar).getManifest().getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
	}


	private void authenticateStrapCode() throws Exception {
		MessageDigest digester = MessageDigest.getInstance("SHA-512", "SUN");
		byte[] hash = digester.digest(_strapCode);
		
		for (byte b : hash) {
			System.out.print("," + b);
		}
		
		if (!Arrays.equals(hash, expectedHash())) throw new Exception("Authentication failed. Your friend might be running a tampered version of Sneer.");
	}

protected byte[] expectedHash() {  //Not private for testing purposes.
	return new byte[]{1, 2, 3};
}

	
	private File appDirectory() {
		File result = new File(sneerDirectory(), "application");
		result.mkdir();
		return result;
	}

	private void closeConnectionToPeer() throws IOException {
		if (_objectIn != null) _objectIn.close();
		if (_socket != null) _socket.close();
	}

	protected byte[] receiveByteArray() throws Exception {  //Not private for testing purposes.
		return (byte[])_objectIn.readObject();
	}

	private File sneerDirectory() {
		File result = new File(System.getProperty("user.home"), ".sneer");
		result.mkdir();
		return result;
	}

	private void openConnectionToPeer() throws Exception {
		_socket = new Socket(_host, _port);
		_objectIn = new ObjectInputStream(_socket.getInputStream());
		
		ObjectOutputStream output = new ObjectOutputStream(_socket.getOutputStream());
		output.writeObject(strapClassName());
	}


	private void executeMainApp() throws Exception {
		executeClass(mainAppFile(), "Main");
	}

	private void executeClass(File jar, String className, String... args) throws ClassNotFoundException, MalformedURLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = new URLClassLoader(new URL[] { jar.toURI().toURL() }).loadClass(className);
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
		showMessage(message, JOptionPane.ERROR_MESSAGE, "Close");
	}
}