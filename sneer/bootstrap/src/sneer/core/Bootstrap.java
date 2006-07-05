package sneer.core;

import static org.eclipse.jdt.internal.compiler.batch.Main.compile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

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
		compileSourceIfNecessary();
		executeMainApp();
	}


	private static void downloadMainAppIfNecessary() throws Exception {
		if (isMainAppAlreadyDownloaded()) return;
		downloadMainApp();
	}

	private static boolean isMainAppAlreadyDownloaded() {
		return mainAppJarFile().exists();
	}

	private static void compileSourceIfNecessary() throws IOException{
		File chook = new File(System.getProperty("user.home") + 
						 File.separator + ".sneer" + 
						 File.separator + "CompilerHook.java");
		File dest = new File(chook.getParentFile(),"application/class" );
//		File jar = new File(chook.getParentFile(),"application/MainApplication.jar");
		if(dest.exists()) return;
//		if(jar.exists()) return;
		
		dest.mkdirs();
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(chook);
			fw.write("public class CompilerHook extends Main {}");
		} finally{
			if(fw!=null){
				fw.close();
			}			
		}
		
		String cmd = "-source 1.6 -target 1.6 " +
				" -d \"" + dest.getAbsolutePath() + 
				"\" -sourcepath \"" + mainAppJarFile().getAbsolutePath() + 
				"\" \"" + chook.getAbsolutePath() + "\"";
		System.out.println(cmd);
		compile(cmd);
		
//		FileOutputStream os = new FileOutputStream(jar);
//		JarOutputStream jos = new JarOutputStream(os, manifest());
//		addJarEntries(jar, jos);
//		jos.close();		
	}

//	private static void addJarEntries(File dir, JarOutputStream jos){
//		File files[] = dir.listFiles();
//		
//		for (File file : files) {
//			if(file.isDirectory()){
//				addJarEntries(file, jos);
//			}else{
//				ZipEntry entry = new ZipEntry(resourceName(clazz));		
//				jos.putNextEntry(entry);
//				jos.write(readClassBytes(clazz));
//				jos.closeEntry();			
//			}
//		}		
//	}
//	
//	
//	
//	private static Manifest manifest() {
//		Manifest m = new Manifest();		
//		Attributes attributes = m.getMainAttributes();
//		attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
//		attributes.put(Attributes.Name.MAIN_CLASS, "Main");
//		return m;
//	}	
//	
	private static File mainAppJarFile() {
		String path =
			System.getProperty("user.home") + File.separator +
			".sneer" + File.separator +
			"application" + File.separator +
			"MainApplication.zip";
		File tmp = new File(path);
		tmp.getParentFile().mkdirs();
		return tmp;
	}

	private static void executeMainApp() throws Exception {
		File jar = mainAppJarFile();
		Class<?> clazz = new URLClassLoader(new URL[] { jar.toURI().toURL() }).loadClass("Main");
		clazz.getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { new String[0] });
	}
	
	private static void downloadMainApp() throws Exception {
		String hostnameAndPort = askHostnameAndPort();
		
		Socket socket = new Socket(host(hostnameAndPort), port(hostnameAndPort));
		byte[] newVersion = null;
		try {
			newVersion = (byte[])new ObjectInputStream(socket.getInputStream()).readObject();
		} finally {
			socket.close();
		}
		saveApp(newVersion);
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

	private static void saveApp(byte[] newVersion) throws IOException {
		File directory = mainAppJarFile().getParentFile();
		directory.mkdirs();
		FileOutputStream fos = new FileOutputStream(mainAppJarFile());
		try {
			fos.write(newVersion);
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