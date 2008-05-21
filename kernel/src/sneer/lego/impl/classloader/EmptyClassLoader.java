package sneer.lego.impl.classloader;

public class EmptyClassLoader extends ClassLoader {

	private static final ClassLoader INSTANCE = new EmptyClassLoader();
	
	private EmptyClassLoader() {}
	
	public static final ClassLoader instance() {
		return INSTANCE;
	}
	
	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		throw new ClassNotFoundException();
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		throw new ClassNotFoundException();
	}
	
}
