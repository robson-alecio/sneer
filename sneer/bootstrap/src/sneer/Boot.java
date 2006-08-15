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
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;

public class Boot {
	
	private static final String TITLE = "Sneer Friend-to-Friend Installation";
	
	private static Socket _socket;
	private static ObjectInputStream _objectIn;

	private static String _host = null;
	private static int _port = 0;

	private static String _address = "Ask your friend what to type in here.";

	
	public static void main(String[] ignored) {
		try {
			boot();
		} catch (Throwable t) {			
			showError(t);
		}
	}

	private static void boot() throws Exception {
		while (!mainAppInstalled()) tryToInstallMainAppFromPeer();
		executeMainApp();
	}

	private static boolean mainAppInstalled() {
		return mainAppFile() != null;
	}

	private static File mainAppFile() {
		return null;
	}

//	private static File lastValidAppJarFile() {
//		while (true) {
//			File candidate = lastAppJarFile();
//			if (candidate == null) return null;
//			if (isValidSignature(candidate)) return candidate;
//			deleteSignedFile(candidate);
//		}
//	}


	private static void tryToInstallMainAppFromPeer() throws Exception, IOException {
		try{
			promptForHostnameAndPort();
			openConnectionToPeer();
			receiveMainApp();
		} catch (Exception x) {
			int option = JOptionPane.showOptionDialog(null, interpret(x), "Error", JOptionPane.NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{"< Back", "Close"}, "< Back");
			if (option != 0) System.exit(0);
		} finally {
			closeConnectionToPeer();
		}
	}


	private static String interpret(Exception x) {
		return x.getClass().getSimpleName().replaceAll("Exception", " ") + x.getMessage();
	}

	private static void showMessage(String message, int type, String okButton) {
		JOptionPane.showOptionDialog(null, message, TITLE, 0, type, null, new Object[]{okButton}, okButton);
	}

//	private static void compileMainApp() throws Exception {
//		delete(tempDirectory());
//		extractMainAppSource();
//		compileMainAppSource();
//	}
//
//	private static void compileMainAppSource() throws Exception {
//		File dest = new File(tempDirectory(), "classes");
//		dest.mkdir();
//
//		execute(compilerJar(), 
//			"-source", "1.6",
//			"-target", "1.6",
//			"-d", dest.getAbsolutePath(),
//			sourceDirectory().getAbsolutePath()
//		);
//
//		
// //FileOutputStream os = new FileOutputStream(jar);
// //JarOutputStream jos = new JarOutputStream(os, manifest());
// //addJarEntries(jar, jos);
// //jos.close();		
//
//	}



	private static void execute(File jar, String... args) throws Exception {
		executeClass(jar, mainClass(jar), args);
	}

	private static String mainClass(File jar) throws IOException {
		return new JarFile(jar).getManifest().getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
	}

//	private static void delete(File file) throws IOException {
//		if (file.isDirectory()) {
//			for (File subFile : file.listFiles()) delete(subFile);
//			return;
//		}
//		if (!file.delete()) throw new IOException("Unable to delete file " + file);
//	}

//	private static void extractMainAppSource() throws Exception {
//		ZipFile sources = new ZipFile(mainAppSourceFile());
//		Enumeration<? extends ZipEntry> entries = sources.entries();
//		while (entries.hasMoreElements()) {
//			extractMainAppSourceEntry(sources, entries.nextElement());
//		}
//	}

//	private static void extractMainAppSourceEntry(ZipFile sources, ZipEntry entry) throws IOException {
//		if (entry.isDirectory()) return;
//		
//		InputStream inputStream = sources.getInputStream(entry);
//		byte[] buffer = new byte[1024*4];
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		int read;
//		while (-1 != (read = inputStream.read(buffer))) {
//			bos.write(buffer, 0, read);
//		}
//		inputStream.close();
//		
//		File sourceFile = new File(sourceDirectory(), entry.getName());
//		save(sourceFile, bos.toByteArray());
//	}

//	private static File tempDirectory() {
//		File result = new File(sneerDirectory(), "temp");
//		result.mkdir();
//		return result;
//	}

	private static void receiveMainApp() throws Exception {
		byte[] jarContents = receiveByteArray();
		byte[] signature = receiveByteArray();
	
		//saveSignedFile(firstAppJar(), jarContents, signature);
	}

//	private static File firstAppJar() {
//		return new File(appDirectory(), "0000000000.jar");
//	}



//	private static void saveSignedFile(File file, byte[] contents, byte[] signature) throws IOException {
//		File signatureFile = new File(file.getAbsolutePath() + ".signature");
//		save(signatureFile, signature);
//		save(file, contents);
//	}

	private static void checkHash(byte[] jarContents, byte[] signature) {
		System.err.println("SHA-512");
	}

//	private static File mainAppSourceFile() {
//		return new File(sneerDirectory(), "MainApplication.zip");
//	}
	
//	private static File lastAppJarFile() {
//		File[] versions = appDirectory().listFiles();
//
//		File result = null;
//		for (File version : versions) {
//			String name = version.getName();
//			if (!name.endsWith(".jar")) continue;
//			if (result == null) result = version;
//			if (name.compareTo(result.getName()) > 0) result = version;
//		}
//		return result;
//	}
	
	private static File appDirectory() {
		File result = new File(sneerDirectory(), "application");
		result.mkdir();
		return result;
	}

	private static void closeConnectionToPeer() throws IOException {
		if (_objectIn != null) _objectIn.close();
		if (_socket != null) _socket.close();
	}

//	private static void receiveCompiler() throws Exception {
//		receiveFileContents(compilerJar());
//	}

//	private static File compilerJar() {
//		return new File(sneerDirectory(), "compiler.jar");
//	}

//	private static void receiveFileContents(File file) throws Exception {
//		save(file, receiveByteArray());
//	}

	private static byte[] receiveByteArray() throws Exception {
		return (byte[])_objectIn.readObject();
	}

	private static File sneerDirectory() {
		File result = new File(System.getProperty("user.home"), ".sneer");
		result.mkdir();
		return result;
	}

	private static void openConnectionToPeer() throws Exception {
		_socket = new Socket(_host, _port);
		_objectIn = new ObjectInputStream(_socket.getInputStream());
		
		ObjectOutputStream output = new ObjectOutputStream(_socket.getOutputStream());
		output.writeObject("Bootstrap");
	}


//	private static void addJarEntries(File dir, JarOutputStream jos){
//		File files[] = dir.listFiles();
//		
//		for (File file : files) {
//			if(file.isDirectory()){
//				addJarEntries(file, jos);
//			} else {
//				ZipEntry entry = new ZipEntry(resourceName(clazz));	
//				jos.putNextEntry(entry);
//				jos.write(readClassBytes(clazz));
//				jos.closeEntry();			
//			}
//		}		
//	}


	private static void executeMainApp() throws Exception {
		executeClass(mainAppFile(), "Main");
	}

	private static void executeClass(File jar, String className, String... args) throws ClassNotFoundException, MalformedURLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = new URLClassLoader(new URL[] { jar.toURI().toURL() }).loadClass(className);
		clazz.getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { args });
	}
	
	private static void promptForHostnameAndPort() throws Exception {
//		String message =
//			" Welcome.  :)\n\n" +
//			" Get a sovereign friend to help you install Sneer\n" +
//			" and guide your first steps.\n\n" +
//			" Sneer will be downloaded from your friend's\n" +
//			" machine, authenticated, and installed.\n\n" +
//			" Enter your friend's host address and Sneer port:";
		String message =
			" Welcome.  :)\n\n" +
			" You will need an expert sovereign friend to guide\n" +
			" your first steps in sovereign computing.\n\n" +
			" Ask your friend what to type here:";
		_address = (String)JOptionPane.showInputDialog(null, message, TITLE, JOptionPane.PLAIN_MESSAGE, null, null, null);
		if (_address == null) System.exit(0);
		parse(_address);
	}

	private static void parse(String address) throws Exception {
		try {
			tryToParse(address);
		} catch (RuntimeException e) {
			throw new Exception("Address must be in hostaddress:port format.");
		}
	}

	private static void tryToParse(String address) {
		String[] parts = address.split(":");
		_host = parts[0];
		_port = Integer.parseInt(parts[1]);
	}

//	private static void save(File file, byte[] contents) throws IOException {
//		FileOutputStream fos = new FileOutputStream(file);
//		try {
//			fos.write(contents);
//		} finally {
//			fos.close();
//		}
//	}

	private static void showError(Throwable t) {
		String message = "There was an unexpected error:\n" +
			t + "\n\n" +
			"Sneer will now close.";
		showMessage(message, JOptionPane.ERROR_MESSAGE, "Close");
	}
}