package sneer.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;

public class Bootstrap {
	
	private static final int DEFAULT_DOWNLOAD_PORT = 42;

	public static void main(String[] ignored) {
		try {
			boot();
		} catch (Exception e) {			
			showExceptionDialog(e);
		}
	}

	private static void boot() throws Exception {
		downloadMainAppIfNecessary();
		executeMainApp();
	}


	private static void downloadMainAppIfNecessary() throws Exception {
		if (isMainAppAlreadyDownloaded()) return;
		downloadMainApp();
	}

	private static boolean isMainAppAlreadyDownloaded() {
		return mainAppJarFile().exists();
	}

	private static File mainAppJarFile() {
		String path =
			System.getProperty("user.home") + File.separator +
			".sneer" + File.separator +
			"application" + File.separator +
			"MainApplication.jar";
		return new File(path);
	}

	private static void executeMainApp() throws Exception {
		File jar = mainAppJarFile();
		String mainClass = getMainClass(jar);
		Class<?> clazz = new URLClassLoader(new URL[] { jar.toURI().toURL() }).loadClass(mainClass);
		clazz.getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { new String[0] });
	}

	private static String getMainClass(File appFile) throws IOException {
		JarFile jar = new JarFile(appFile);		
		try {
			return jar.getManifest().getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
		} finally {
			jar.close();
		}
	}
	
	private static void downloadMainApp() throws Exception {
		String hostnameAndPort = askHostnameAndPort();
		
		Socket socket = new Socket(host(hostnameAndPort), port(hostnameAndPort));
		try {
			Object newVersion = new ObjectInputStream(socket.getInputStream()).readObject();
			saveApp(newVersion);
		} finally {
			socket.close();
		}
	}
	
	public static String host(String s) {
		String[] addressParts = s.split(":");
		return addressParts[0];
	}
	
	public static int port(String s) {
		String[] addressParts = s.split(":");
		return addressParts.length > 1 
			? Integer.parseInt(addressParts[1])
			: DEFAULT_DOWNLOAD_PORT;
	}

	private static String askHostnameAndPort() {
		return JOptionPane.showInputDialog(null, "What is your contact's address? host:port", "localhost:42");
	}

	private static void saveApp(Object newVersion) throws IOException {
//		File directory = new File(path);
//		directory.mkdirs();
		FileOutputStream fos = new FileOutputStream(mainAppJarFile());
		try {
			fos.write((byte[])newVersion);
		} finally {
			fos.close();
		}
	}



	private static void showExceptionDialog(Exception e) {
		StringWriter message = new StringWriter();
		message.write(e.toString() + "\n");
		e.printStackTrace(new PrintWriter(message));
		JOptionPane.showMessageDialog(null, message.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
	}

}
