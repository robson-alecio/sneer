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
import java.util.Arrays;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;

public class SacredLoader {
	
	private static final int DEFAULT_DOWNLOAD_PORT = 42;

	public static void main(String[] args) {
		String path = applicationPath();
		
		try {
			String application = findNewestApp(path);
			if (null == application) {
				downloadApp();
				application = findNewestApp(path);
			}
			executeApp(application);
		} catch (Exception e) {			
			StringWriter message = new StringWriter();
			message.write(e.toString() + "\n");
			e.printStackTrace(new PrintWriter(message));
			JOptionPane.showMessageDialog(null, message.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	private static void executeApp(String application) throws Exception {
		
		File appFile = new File(applicationPath(), application);
		String mainClass = getMainClass(appFile);
		Class<?> clazz = new URLClassLoader(new URL[] { appFile.toURL() }).loadClass(mainClass);
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
	
	private static void downloadApp() throws IOException {
		
		String hostnameAndPort = askHostnameAndPort();
		
		Socket socket = new Socket(host(hostnameAndPort), port(hostnameAndPort));
		try {
			Object newVersion = new ObjectInputStream(socket.getInputStream()).readObject();
			saveApp(newVersion);
		} catch (ClassNotFoundException e) {			
			throw new RuntimeException(e);
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
		FileOutputStream fos = new FileOutputStream(firstJarName());
		try {
			fos.write((byte[])newVersion);
		} finally {
			fos.close();
		}
	}

	private static String firstJarName() {
		return applicationPath() + File.separator + "0000000000000000000.jar"; 
	}

	private static String applicationPath() {
		String userHome = System.getProperty("user.home");
		String separator = File.separator;
		return userHome + separator + ".sneer" + separator + "application";
	}

	private static String findNewestApp(String path) throws IOException {
		File directory = new File(path);
		directory.mkdirs();
		
		String[] fileNames = directory.list();
		if (fileNames.length == 0) return null;
		
		Arrays.sort(fileNames);
		return fileNames[fileNames.length-1];
	}

}
