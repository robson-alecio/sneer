package sneer;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
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

	protected void run() { //Not private for testing purposes.
		try {
			tryToRun();
		} catch (Throwable t) {			
			showError(t);
		}
	}

	private void tryToRun() throws Exception {
		while (!mainAppInstalled())
			tryToInstallMainAppFromPeer("< Back");
		executeMainApp();
	}

	private boolean mainAppInstalled() {
		return mainAppFile() != null;
	}

	private File mainAppFile() {
		return null;
	}

//	private File lastValidAppJarFile() {
//		while (true) {
//			File candidate = lastAppJarFile();
//			if (candidate == null) return null;
//			if (isValidSignature(candidate)) return candidate;
//			deleteSignedFile(candidate);
//		}
//	}


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


	private void runStrapFromPeer() throws Exception {
			_strapCode = receiveByteArray();
			checkHash();
	        Class<?> clazz = defineClass("sneer.Strap", _strapCode, 0, _strapCode.length);
			clazz.getMethod("run", new Class[] {}).invoke(null, new Object[] {});
	}

	private String interpret(Exception x) {
		return x.getClass().getSimpleName().replaceAll("Exception", " ") + x.getMessage();
	}

	private void showMessage(String message, int type, String okButton) {
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



	private void execute(File jar, String... args) throws Exception {
		executeClass(jar, mainClass(jar), args);
	}

	private String mainClass(File jar) throws IOException {
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



//	private static File firstAppJar() {
//		return new File(appDirectory(), "0000000000.jar");
//	}



//	private static void saveSignedFile(File file, byte[] contents, byte[] signature) throws IOException {
//		File signatureFile = new File(file.getAbsolutePath() + ".signature");
//		save(signatureFile, signature);
//		save(file, contents);
//	}

	private void checkHash() {
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
	
	private File appDirectory() {
		File result = new File(sneerDirectory(), "application");
		result.mkdir();
		return result;
	}

	private void closeConnectionToPeer() throws IOException {
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
		output.writeObject("Strap");
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


	private void executeMainApp() throws Exception {
		executeClass(mainAppFile(), "Main");
	}

	private void executeClass(File jar, String className, String... args) throws ClassNotFoundException, MalformedURLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = new URLClassLoader(new URL[] { jar.toURI().toURL() }).loadClass(className);
		clazz.getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { args });
	}
	
	protected String promptForHostnameAndPort() throws Exception {  //Not private for testing purposes.
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



	
//	private static void save(File file, byte[] contents) throws IOException {
//		FileOutputStream fos = new FileOutputStream(file);
//		try {
//			fos.write(contents);
//		} finally {
//			fos.close();
//		}
//	}

	private void showError(Throwable t) {
		String message = "There was an unexpected error:\n" +
			t + "\n\n" +
			"Sneer will now close.";
		showMessage(message, JOptionPane.ERROR_MESSAGE, "Close");
	}
}