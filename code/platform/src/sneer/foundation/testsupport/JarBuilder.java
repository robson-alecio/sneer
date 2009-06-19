package sneer.foundation.testsupport;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

@Deprecated
public class JarBuilder implements Closeable{

	private final JarOutputStream _out;
	
	public JarBuilder(File file) throws IOException {
		_out = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
	}
	
	public void add(String entryName, File file) throws IOException {
		add(entryName, new FileInputStream(file.getAbsolutePath()));
	}

	public void add(String entryName, String contents) throws IOException {
		add(entryName, new ByteArrayInputStream(contents.getBytes()));
	}
	
	private void add(String entryName, InputStream is) throws IOException {
		entryName = entryName.replace('\\', '/');
		JarEntry je = new JarEntry(entryName);
		_out.putNextEntry(je);
		byte[] buffer = new byte[1024*4];
		int n = 0;
		while (-1 != (n = is.read(buffer)))  _out.write(buffer, 0, n); 
		is.close();
	}
	
    public void close() {
		try { _out.close(); } catch (Throwable ignore) { }
	}
}