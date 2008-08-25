package sneer.kernel.container.impl.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import sneer.pulp.config.SneerConfig;
		
public class ApiClassLoader extends URLClassLoader {

	private final SneerConfig _config;

	public ApiClassLoader(SneerConfig config, ClassLoader parent) {
		super(new URL[0], parent);
		_config = config;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		File brickDirectory = new File(_config.brickRootDirectory(), name);
		if (!brickDirectory.exists())
//			throw new ClassNotFoundException(name);
			return null;
		
		try {
			addURL(new File(brickDirectory, name + "-api.jar").toURI().toURL());
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
		return super.findClass(name);
	}
}
