package sneer.lego.impl.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;

import sneer.bricks.dependency.Dependency;

public class BrickClassLoader extends EnhancingClassLoader {

	private File _brickDirectory;
	
	private Class<?> _mainClass;
	
	private File _implJarFile;
	
	private Map<String, byte[]> _cache; 
	
	private ClassLoader _delegate;
	
	public BrickClassLoader(ClassLoader parent, Class<?> mainClass, File brickDirectory, List<Dependency> dependencies) {
		super(parent);
		_brickDirectory = brickDirectory;
		_mainClass = mainClass;
		_implJarFile = new File(_brickDirectory, _mainClass.getName()+"-impl.jar");
		_delegate = delegate(dependencies);
	}

	private ClassLoader delegate(List<Dependency> dependencies) {
		if(dependencies == null)
			return null;
		
		URL[] urls = new URL[dependencies.size()];
		int i = 0;
		for (Dependency dependency : dependencies) {
			try {
				urls[i++] = new URL("file://"+dependency.file().getAbsolutePath());
			} catch (MalformedURLException e) {
				throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
			}
		}
		return new URLClassLoader(urls, null);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] bytes;
		try {
			bytes = findClassInJar(name);
		} catch (IOException e) {
			throw new ClassNotFoundException("Error loading bytes from "+_implJarFile);
		}
		
		if(bytes != null)
			return defineClass(name, bytes);
			
		//is it a brick dependency?
		if(_delegate != null) {
			Class<?> result = _delegate.loadClass(name);
			if(result != null)
				return result;
		}

		//delegate to parent class loader
		throw new ClassNotFoundException();
	}

	private byte[] findClassInJar(String name) throws IOException {
		
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

	public String mainClass() {
		return _mainClass.getName();
	}

	@Override
	public String toString() {
		return "BrickClassLoader ["+mainClass()+"] "+_brickDirectory;
		
	}
}
