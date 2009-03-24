package sneer.kernel.container.impl.classloader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wheel.io.codegeneration.MetaClass;

public class MetaClassClassLoader extends EnhancingClassLoader {

	private Map<String, MetaClass> _hash;

	public MetaClassClassLoader(List<MetaClass> files, ClassLoader parent) {
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
	protected Class<?> findClass2(String name) {
		MetaClass meta = _hash.get(name);
		if (meta == null) return null;
		try {
			return defineClass(meta.getName(), meta.bytes());
		} catch (IOException e) {
			throw new IllegalStateException("Error reading bytes from " + meta.classFile().getName());
		}
	}
}
