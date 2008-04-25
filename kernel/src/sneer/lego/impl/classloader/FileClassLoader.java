package sneer.lego.impl.classloader;

import java.io.IOException;
import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sneer.lego.impl.classloader.enhancer.ByteCodeGuardian;
import sneer.lego.impl.classloader.enhancer.EnhancingByteCodeGuardian;
import sneer.lego.utils.metaclass.MetaClass;

public class FileClassLoader extends SecureClassLoader {

	private List<MetaClass> _metaClasses;

	private String _name;

	private Map<String, MetaClass> _hash;
	
	private ByteCodeGuardian _guardian = EnhancingByteCodeGuardian.instance();

	public FileClassLoader(String name, List<MetaClass> files, ClassLoader parent) {
		super(parent);
		_name = name;
		_metaClasses = files;
		_hash = computeHash(_metaClasses);
	}

	private Map<String, MetaClass> computeHash(List<MetaClass> metaClasses) {
		Map<String, MetaClass> result = new HashMap<String, MetaClass>();
		for (MetaClass meta : metaClasses) {
			String className = meta.getName();
			result.put(className, meta);
		}
		return result;
	}

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		// First, check if the class has already been loaded
		ClassLoader parent = getParent();
		Class<?> c = findLoadedClass(name);
		if (c == null) {
			try {
				c = findClass(name);
			} catch (ClassNotFoundException e) {
				if (parent != null) {
					c = parent.loadClass(name);
				} else {
					//c = findBootstrapClass0(name);
				}
			}
		}
		if (resolve) {
			resolveClass(c);
		}
		return c;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		MetaClass meta = _hash.get(name);
		if (meta == null)
			throw new ClassNotFoundException("Class not found " + name);

		try {
			return defineClass(name, meta.bytes());
		} catch (IOException e) {
			throw new ClassNotFoundException("Error reading bytes from " + meta.classFile().getName());
		}
	}

	private Class<?> defineClass(String name, byte[] byteArray) {
		byteArray = _guardian.enhance(name, byteArray);
		return defineClass(name, byteArray, 0, byteArray.length);
	}


	@Override
	public String toString() {
		return _name + " (" + this.getClass().getName() + ")";
	}

	public void debug() {
		System.out.println(" ** " + _name + " ** ");
		for (MetaClass metaClass : _metaClasses) {
			System.out.println(" " + metaClass.getName());
		}
	}
}
