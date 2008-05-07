package sneer.lego.impl.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;

public class BrickClassLoader extends EnhancingClassLoader {

	private File _brickDirectory;
	
	private Class<?> _mainClass;
	
	private File _implJarFile;
	
	private Map<String, byte[]> _cache; 
	
	public BrickClassLoader(ClassLoader parent, Class<?> mainClass, File brickDirectory) {
		super(parent);
		_brickDirectory = brickDirectory;
		_mainClass = mainClass;
		_implJarFile = new File(_brickDirectory, _mainClass.getName()+"-impl.jar");
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] bytes;
		try {
			bytes = openJar(name);
		} catch (IOException e) {
			throw new ClassNotFoundException("Error loading bytes from "+_implJarFile);
		}
		
		//delegate to parent class loader
		if(bytes == null) 
			throw new ClassNotFoundException();
		
		return defineClass(name, bytes);
	}

	private byte[] openJar(String name) throws IOException {
		
		if(_cache != null) return _cache.get(name);
		
		_cache = new WeakHashMap<String, byte[]>();
		byte[] result = null;

		JarFile jar = new JarFile(_implJarFile);
		Enumeration<JarEntry> e = jar.entries();
		while (e.hasMoreElements()) {
			JarEntry entry = e.nextElement();
			byte[] byteArray = readEntry(jar, entry);
			String entryName = entry.getName();
			String key = cache(entryName, byteArray);
			if(key.equals(name)) 
				result = byteArray;
		}
		return result;
	}


	private String cache(String entryName, byte[] byteArray) {
		String key = entryName.replaceAll("/", ".");
		int index = key.indexOf(".class");
		if(index > 0) {
			key = key.substring(0, index);
		}
		_cache.put(key, byteArray);
		return key;
	}

	private byte[] readEntry(JarFile jar, JarEntry entry) throws IOException {
		InputStream is = null;
		try {
			is = jar.getInputStream(entry);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			IOUtils.copy(is, os);
			return os.toByteArray();
		} finally {
			if(is != null)
				IOUtils.closeQuietly(is);
		}
	}

	public String getMainClass() {
		return _mainClass.getName();
	}
}
