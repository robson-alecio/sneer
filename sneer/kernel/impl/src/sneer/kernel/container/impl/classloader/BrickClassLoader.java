package sneer.kernel.container.impl.classloader;

import static wheel.io.Logger.log;
import static wheel.lang.Environments.my;

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

import sneer.pulp.dependency.Dependency;
import sneer.pulp.dependency.DependencyManager;
import wheel.lang.Threads;

public class BrickClassLoader extends EnhancingClassLoader {

	private final File _brickDirectory;
	
	private final Class<?> _mainClass;
	
	private final File _implJarFile;
	
	private Map<String, byte[]> _cache; 
	
	private ClassLoader _delegate;
	
	private final DependencyManager _dependencyManager = my(DependencyManager.class);
	
	public BrickClassLoader() {
		//used for testing
		this(Threads.contextClassLoader(), Object.class, null);
	}
	
	public BrickClassLoader(ClassLoader parent, Class<?> mainClass, File brickDirectory) {
		super(parent);
		_brickDirectory = brickDirectory;
		_mainClass = mainClass;
		_implJarFile = new File(_brickDirectory, _mainClass.getName()+"-impl.jar");
	}

	@Override
	protected Class<?> findClass2(String name) {
		byte[] bytes;
		try {
			bytes = findClassInJar(name);
		} catch (IOException e) {
			throw new IllegalStateException("Error loading bytes from "+_implJarFile);
		}
		
		if(bytes != null) {
			Class<?> result  = defineClass(name, bytes);
			log("Class {} loaded by {}", name, toString());
			return result;
		}
			
		//is it a brick dependency?
		try {
			Class<?> result = delegate().loadClass(name);
			if(result != null) {
				log("Class {} loaded by _delegate_ ",name, toString());			
				return result;
			}
		} catch (ClassNotFoundException e) {
		}
		return null;
	}

	private ClassLoader delegate() {
		if(_delegate != null) 
			return _delegate;

		List<Dependency> dependencies = _dependencyManager.dependenciesFor(_mainClass.getName());
		
		if(dependencies == null) {
			_delegate = EmptyClassLoader.instance(); 
			return _delegate; 
		}
		
		_delegate = new URLClassLoader(urlsForDependencies(dependencies), getParent());
		return _delegate;
	}

	private URL[] urlsForDependencies(List<Dependency> dependencies) {
		URL[] urls = new URL[dependencies.size()];
		int i = 0;
		for (Dependency dependency : dependencies) {
			try {
				urls[i++] = dependency.file().toURI().toURL();
			} catch (MalformedURLException e) {
				throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
			}
		}
		return urls;
	}

	private byte[] findClassInJar(String name) throws IOException {
		
		if(_cache != null) return _cache.get(name);
		
		_cache = new WeakHashMap<String, byte[]>();

		final JarFile jar = new JarFile(_implJarFile);
		try {
			Enumeration<JarEntry> e = jar.entries();
			while (e.hasMoreElements()) {
				JarEntry entry = e.nextElement();
				byte[] byteArray = readEntry(jar, entry);
				String entryName = entry.getName();
				String key = cache(entryName, byteArray);
				if(key.equals(name)) 
					return byteArray;
			}
			return null;
		} finally { 
			jar.close();
		} 
	}
			
	private String cache(String entryName, byte[] byteArray) {
		final String key = classNameGiven(entryName);
		_cache.put(key, byteArray);
		return key;
	}

	private String classNameGiven(String entryName) {
		String key = entryName.replaceAll("/", ".");
		int index = key.indexOf(".class");
		if(index > 0) {
			key = key.substring(0, index);
		}
		return key;
	}

	private byte[] readEntry(JarFile jar, JarEntry entry) throws IOException {
		final InputStream is = jar.getInputStream(entry);
		try {
			return IOUtils.toByteArray(is);
		} finally {
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
