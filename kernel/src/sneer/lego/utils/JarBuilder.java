package sneer.lego.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.apache.commons.io.IOUtils;

public class JarBuilder {
	
	private JarOutputStream _out;

	private String _path;
	
	private JarBuilder(JarOutputStream out, String path) {
		_out = out;
		_path = path;
	}
	
	public static JarBuilder builder(String path) throws IOException {
		JarOutputStream out = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
		return new JarBuilder(out, path);
	} 

	public void add(String entryName, File file) throws IOException {
		JarEntry je = new JarEntry(entryName);
		_out.putNextEntry(je);	
		
		InputStream is = new FileInputStream(file.getAbsolutePath());
		IOUtils.copy(is, _out); //This method buffers the input internally, so there is no need to use a BufferedInputStream.
		is.close();
	}

	public File close() {
		IOUtils.closeQuietly(_out);
		return new File(_path);
	}
}
