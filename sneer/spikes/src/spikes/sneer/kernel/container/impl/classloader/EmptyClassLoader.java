package spikes.sneer.kernel.container.impl.classloader;

public class EmptyClassLoader extends ClassLoader {

	private static final ClassLoader INSTANCE = new EmptyClassLoader();
	
	private EmptyClassLoader() {}
	
	public static final ClassLoader instance() {
		return INSTANCE;
	}
	
	@Override
	public Class<?> loadClass(String className) {
		return null;
	}

	@Override
	protected Class<?> findClass(String name) {
		return null;
	}
	
}
