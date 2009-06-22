package sneer.bricks.software.code.jar.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import sneer.bricks.software.code.jar.JarBuilder;
import sneer.bricks.software.code.jar.Jars;

public class JarsImpl implements Jars {

	@Override
	public JarBuilder builder(File file) throws IOException {
		return new JarBuilderImpl(file);
	}

}

class JarBuilderImpl implements JarBuilder{

	private final JarOutputStream _out;
	
	public JarBuilderImpl(File file) throws IOException {
		_out = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
	}
	
	@Override
	public void add(String entryName, File file) throws IOException {
		add(entryName, new FileInputStream(file.getAbsolutePath()));
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
	
	@Override
	public void close() {
		try { _out.close(); } catch (Throwable ignore) { }
	}
}