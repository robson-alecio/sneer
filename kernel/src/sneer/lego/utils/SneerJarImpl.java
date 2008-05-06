package sneer.lego.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import sneer.bricks.crypto.Crypto;
import sneer.bricks.crypto.Digester;
import sneer.lego.Inject;
import wheel.lang.StringUtils;
import wheel.lang.exceptions.NotImplementedYet;

public class SneerJarImpl implements SneerJar {

	private JarOutputStream _out;

	private File _file;
	
	private JarFile _jarFile;

	private Properties _properties;

	private byte[] _sneer1024;
	
	@Inject
	private Crypto _crypto;

	public SneerJarImpl(File file) {
		_file = file;
	}

	public void add(String entryName, File file) throws IOException {
		add(entryName, new FileInputStream(file.getAbsolutePath()));
	}

	public void add(String entryName, String contents) throws IOException {
		add(entryName, new ByteArrayInputStream(contents.getBytes()));
	}
	
	public void add(String entryName, InputStream is) throws IOException {
		JarEntry je = new JarEntry(entryName);
		outputStream().putNextEntry(je);
		IOUtils.copy(is, _out); //This method buffers the input internally, so there is no need to use a BufferedInputStream.
		is.close();
	}

	private JarOutputStream outputStream() throws IOException {
		if(_out != null)
			return _out;
		
		_out = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(_file)));
		return _out;
	}

	@Override
	public void close() throws IOException {
		IOUtils.closeQuietly(outputStream());
		_jarFile = new JarFile(_file);
	}

	@Override
	public InputStream getInputStream(String entryName) throws IOException {
		return _jarFile.getInputStream(_jarFile.getEntry(entryName));
	}

	public Enumeration<JarEntry> entries() {
		return _jarFile.entries();
	}

	@Override
	public byte[] sneer1024() {
		if(_sneer1024 != null) 
			return _sneer1024;
		
		String role = role();
		if("api".equals(role) || "impl".equals(role)) {
			_sneer1024 = makeHash();
		} else {
			_sneer1024 = "NO DONUT FOR YOU".getBytes();
		}
		return _sneer1024;
	}

	private byte[] makeHash() {
		Digester digester = _crypto.sneer1024();
		Enumeration<JarEntry> e = _jarFile.entries();
		while (e.hasMoreElements()) {
			JarEntry entry = e.nextElement();
			String name = entry.getName();
			if (!entry.isDirectory() && includeInHash(name)) {
				InputStream is = null;
				try {
					is = getInputStream(name);
					//System.out.print("  " + name);
					digester.update(is);
				} catch (IOException ioe) {
					throw new wheel.lang.exceptions.NotImplementedYet(ioe); // Implement Handle this exception.
				} finally {
					if(is != null)
						IOUtils.closeQuietly(is);
				}
			}
		}
		return digester.digest();
	}

	private boolean includeInHash(String name) {
		String role = role();
		if("api".equals(role)) {
			return name.endsWith(".class") && name.indexOf("impl") < 0;
		} else if("impl".equals(role)) {
			return name.endsWith(".class") && name.indexOf("impl") >= 0;
//		} else if("api-src".equals(role)) {
//			return name.endsWith(".java") && name.indexOf("impl") < 0;
//		} else if("impl-src".equals(role)) {
//			return name.endsWith(".java") && name.indexOf("impl") >= 0;
		} else {
			throw new NotImplementedYet();
		}
	}

	@Override
	public File file() {
		return _file;
	}

	@Override
	public void explode(File target) throws IOException {
		Enumeration<JarEntry> e = _jarFile.entries();
		while (e.hasMoreElements()) {
			JarEntry entry = e.nextElement();
			String name = entry.getName();
			if(entry.isDirectory() && !skipDirectory(name)) {
				File dir = new File(target, name);
				dir.mkdirs();
			} else if (!entry.isDirectory() && !skipFile(name)) {
				File file = new File(target, name);
				FileUtils.touch(file);
				InputStream is = getInputStream(name);
				IOUtils.copy(is, new FileOutputStream(file));
				IOUtils.closeQuietly(is);
			}
		}
	}
	
	private boolean skipFile(String name) {
		return name.endsWith("MANIFEST.MF") || name.endsWith("sneer.meta");
	}

	private boolean skipDirectory(String name) {
		return name.endsWith("META-INF/");
	}
	
	private Properties properties() throws IOException {
		
		if(_properties != null)
			return _properties;
		
		Properties result = new Properties();
		InputStream is = getInputStream("sneer.meta"); 
		result.load(is);
		IOUtils.closeQuietly(is);
		_properties = result;
		return result;
	}

	@Override
	public String brickName() {
		return property("brick-name");
	}
	
	@Override
	public String role() {
		return property("role").toLowerCase();
	}
	
	private String property(String propertyName) {
		try {
			return properties().getProperty(propertyName);
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		}
	}

	@Override
	public String toString() {
		return _file.toString();
	}

	public boolean matches(SneerJar other) {
		boolean result = Arrays.equals(sneer1024(), other.sneer1024());
		if(!result) 
			System.out.println("MISMATCH "+_file + ":" + StringUtils.toHexa(sneer1024()) + " " +other.file() + ":" + StringUtils.toHexa(other.sneer1024()));
		else
			System.out.println("MATCH "+_file + " AND " +other.file() + " HASH " + StringUtils.toHexa(other.sneer1024()));
		return result; 
	}
	
	
	
//    public void copy(InputStream input, OutputStream output) throws IOException {
//    	byte[] bytes = new byte[BUFFER_SIZE];
//    	int n = 0;
//    	while (-1 != (n = input.read(bytes))) {
//    		output.write(bytes, 0, n);
//    		_buffer.put(bytes);
//    	}
//    }
}
