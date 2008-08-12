package wheel.io;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.apache.commons.io.IOUtils;

public class JarBuilder {

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
		JarEntry je = new JarEntry(entryName);
		_out.putNextEntry(je);
		IOUtils.copy(is, _out); //This method buffers the input internally, so there is no need to use a BufferedInputStream.
		is.close();
	}
	
	public void close() {
		IOUtils.closeQuietly(_out);
	}
}
