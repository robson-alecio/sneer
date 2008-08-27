package sneer.kernel.container.impl.classloader;

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

import sneer.kernel.container.Container;
import sneer.kernel.container.Inject;
import sneer.pulp.dependency.Dependency;
import sneer.pulp.dependency.DependencyManager;
import sneer.pulp.log.Logger;
import wheel.lang.Threads;

public class BrickClassLoader extends EnhancingClassLoader {

	private File _brickDirectory;
	
	private Class<?> _mainClass;
	
	private File _implJarFile;
	
	private Map<String, byte[]> _cache; 
	
	private ClassLoader _delegate;
	
	@Inject
	private Container _container;

	@Inject
	private Logger _log;
	
	//lazy load
	private DependencyManager _dependencyManager;
	
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
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] bytes;
		try {
			bytes = findClassInJar(name);
		} catch (IOException e) {
			throw new ClassNotFoundException("Error loading bytes from "+_implJarFile);
		}
		
		if(bytes != null) {
			Class<?> result  = defineClass(name, bytes);
			if(_log.isDebugEnabled()) _log.debug("Class {} loaded by {}",name, toString());
			return result;
		}
			
		//is it a brick dependency?
		Class<?> result = delegate().loadClass(name);
		if(result != null) {
			if(_log.isDebugEnabled()) _log.debug("Class {} loaded by _delegate_ ",name, toString());			
			return result;
		}
		
		//delegate to parent class loader
		throw new ClassNotFoundException();
	}

	private ClassLoader delegate() {
		if(_delegate != null) 
			return _delegate;

		List<Dependency> dependencies = dependencyManager().dependenciesFor(_mainClass.getName());
		
		if(dependencies == null) {
			_delegate = EmptyClassLoader.instance(); 
			return _delegate; 
		}
		
		URL[] urls = new URL[dependencies.size()];
		int i = 0;
		for (Dependency dependency : dependencies) {
			try {
				urls[i++] = new URL("file://"+dependency.file().getAbsolutePath());
			} catch (MalformedURLException e) {
				throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
			}
		}
		_delegate = new URLClassLoader(urls, null);
		return _delegate;
	}

	private DependencyManager dependencyManager() {

		//lazy load
		if(_dependencyManager != null)
			return _dependencyManager;
		
		_dependencyManager = _container.produce(DependencyManager.class);
		return _dependencyManager;
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
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			IOUtils.copy(is, os);
			return os.toByteArray();
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
