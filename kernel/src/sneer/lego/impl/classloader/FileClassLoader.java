package sneer.lego.impl.classloader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sneer.lego.utils.metaclass.MetaClass;

public class FileClassLoader extends EnhancingClassLoader {

	private Map<String, MetaClass> _hash;

	public FileClassLoader(List<MetaClass> files, ClassLoader parent) {
		super(parent);
		_hash = computeHash(files);
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
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		MetaClass meta = _hash.get(name);
		if (meta == null) throw new ClassNotFoundException("Class not found " + name);
		try {
			return defineClass(meta.getName(), meta.bytes());
		} catch (IOException e) {
			throw new ClassNotFoundException("Error reading bytes from " + meta.classFile().getName());
		}
	}
}
