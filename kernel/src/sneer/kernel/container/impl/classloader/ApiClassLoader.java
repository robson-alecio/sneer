package sneer.kernel.container.impl.classloader;

import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import sneer.kernel.container.SneerConfig;
		
public class ApiClassLoader extends URLClassLoader {

	private final SneerConfig _config;
	static private final List<Reference<ApiClassLoader>> _instances = new ArrayList<Reference<ApiClassLoader>>();

	public ApiClassLoader(SneerConfig config, ClassLoader parent) {
		super(new URL[0], parent);
		_config = config;
		
		_instances.add(new WeakReference<ApiClassLoader>(this));
		System.out.println("Instances: " + _instances.size());
	}

	static public void checkAllInstancesAreFreed() {
		System.gc();
		for (Reference<ApiClassLoader> ref : _instances) {
			final ApiClassLoader classLoader = ref.get();
			if (classLoader != null) {
				try {
					throw new IllegalStateException("Somebody is still holding references to an ApiClassloader");
				} catch (IllegalStateException e) {
					e.printStackTrace();
					throw e;
				}
			}
		}
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		File brickDirectory = new File(_config.brickRootDirectory(), name);
		if (!brickDirectory.exists())
			throw new ClassNotFoundException(name);
		
		File jar = new File(brickDirectory, name + "-api.jar");
		try {
			addURL(jar.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
//		if (jar.exists() && !jar.delete()) throw new RuntimeException("Before");
//		System.err.println("deleted");
//		System.err.println("before");
		Class<?> result = super.findClass(name);
//		if (jar.exists() && !jar.delete()) throw new RuntimeException("After");
		return result;
	}
}
