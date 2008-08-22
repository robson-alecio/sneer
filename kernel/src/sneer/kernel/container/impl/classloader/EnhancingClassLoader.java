package sneer.kernel.container.impl.classloader;

import java.security.SecureClassLoader;

import sneer.kernel.container.Inject;
import sneer.kernel.container.Injector;
import sneer.kernel.container.impl.classloader.enhancer.ByteCodeGuardian;
import sneer.kernel.container.impl.classloader.enhancer.EnhancingByteCodeGuardian;

public abstract class EnhancingClassLoader extends SecureClassLoader {

	@Inject
	protected Injector _injector;

	private ByteCodeGuardian _guardian = EnhancingByteCodeGuardian.instance();

	public EnhancingClassLoader(ClassLoader parent) {
		super(parent);
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
		if (c == null) {
			throw new ClassNotFoundException(name);
		}
		
		if (resolve) {
			resolveClass(c);
		}
		
		if(!c.isInterface() && _injector != null /* don't remove this check. BrickClassLoaders may be created without an Injector */)
			_injector.inject(c);
		
		return c;
	}

	protected Class<?> defineClass(String name, byte[] bytes) {
		byte[] byteArray = _guardian.enhance(name, bytes);
		return defineClass(name, byteArray, 0, byteArray.length);
	}
}
