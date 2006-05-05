package sneer.core;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

public class SovereignVirus {

	static final String JAR_NAME = "sneer.jar";

	public static void main(String[] args) throws IOException {
		
		startDaemon(new VirusReplicator());
		startDaemon(new HttpServer());
		
		System.out.println("Press certain keys to quit");
		System.in.read();
	}
	
	static void sendSacredLoader(Socket socket) throws IOException {
		
		Class clazz = null;
		
		try {
			clazz = Class.forName("sneer.core.SacredLoader");
		} catch (ClassNotFoundException e) {
			sendPage(socket, "<body>" + e.toString() + "</body>");
			return;
		}
		
		OutputStream os = socket.getOutputStream();
		writeUTF8(os, "HTTP/1.1 200 OK\n");
		writeUTF8(os, "Content-Type: binary/octet-stream\n");
		writeUTF8(os, "Content-Disposition: attachment; filename=sneer.jar\n");
		writeUTF8(os, "\n");
		os.flush();
		writeClassJar(os, clazz);
		os.flush();
		os.close();
	}

	private static void writeUTF8(OutputStream os, String s) throws IOException, UnsupportedEncodingException {
		os.write(s.getBytes("utf-8"));
	}

	static void welcome(Socket socket) throws IOException {
		String html = "<body>"
			+ "<h2>Prepare to become sovereign...</h2>"
			+ "<ul>"
			+ "<li>Install Java JRE 1.5 from <a target='blank' href='http://java.com/'>here</a></li>"
			+ "<li>Download and run <a href='/sneer.jar'>Sneer</a>"
			+ "</body>";
		sendPage(socket, html);
	}

	private static void sendPage(Socket socket, String body) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		writer.write("HTTP 1.0\n");
		writer.write("Content-Type: text/html\n");
		writer.write("\n");
		writer.write("<html><head><title>Sneer - The Sovereign Peer</title></head>");
		writer.write(body);
		writer.write("</html>");
		writer.flush();
		writer.close();
	}

	private static void startDaemon(Runnable closure) {
		Thread thread = new Thread(closure);
		thread.setDaemon(true);
		thread.start();
	}

	static void writeJar(Socket socket) throws IOException {		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		writeClassJar(baos, SovereignVirus.class, HttpServer.class, VirusReplicator.class);
		writeObject(socket, baos.toByteArray());
	}

	private static void writeObject(Socket socket, Object o) throws IOException {
		OutputStream sos = socket.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(sos);		
		oos.writeObject(o);
		oos.flush();
	}

	private static void writeClassJar(OutputStream os, Class ...classes) throws IOException {
		Class mainClass = classes[0];
		JarOutputStream jos = new JarOutputStream(os, manifest(mainClass));
		for (Class clazz : classes) {
			ZipEntry entry = new ZipEntry(resourceName(clazz));		
			jos.putNextEntry(entry);
			jos.write(readClassBytes(clazz));
			jos.closeEntry();
		}
		jos.close();
	}

	private static byte[] readClassBytes(Class clazz) throws IOException {
		return readAllBytes(getResource(clazz));
	}

	private static Manifest manifest(Class mainClass) {
		Manifest m = new Manifest();		
		Attributes attributes = m.getMainAttributes();
		attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0"); // ALWAYS set the MANIFEST_VERSION attribute because of a bug in the SUN class library
		attributes.put(Attributes.Name.MAIN_CLASS, mainClass.getCanonicalName());
		return m;
	}

	private static byte[] readAllBytes(InputStream is) throws IOException {
		int size = is.available();
		byte[] buffer = new byte[size];
		is.read(buffer);
		return buffer;
	}

	private static InputStream getResource(Class clazz) {
		return clazz.getResourceAsStream(clazz.getSimpleName() + ".class");
	}

	private static String resourceName(Class clazz) {
		return clazz.getCanonicalName().replace('.', '/') + ".class";
	}

}
