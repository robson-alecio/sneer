package wheel.io.codegeneration;

import java.io.File;
import java.net.URISyntaxException;


public class ClassUtils {

   public static MetaClass metaClass(File rootDirectory, File classFile) {
	    return new SimpleMetaClass(rootDirectory, classFile);
		//return new LazyMetaClass(rootDirectory, classFile);
	}

	public static MetaClass metaClass(Class<?> clazz) {
	    final File classFile = toFile(clazz);
		return ClassUtils.metaClass(rootDirectoryFor(clazz, classFile), classFile);
	}
	
	public static File rootDirectoryFor(Class<?> clazz) {
		return rootDirectoryFor(clazz, toFile(clazz));
	}

	private static File rootDirectoryFor(Class<?> clazz, File classFile) {
		final int packageCount = packageName(clazz).split("\\.").length;
		
		File parent = classFile.getParentFile();
		for (int i=0; i<packageCount; ++i)
			parent = parent.getParentFile();
		return parent;
	}

	private static String packageName(Class<?> clazz) {
		return clazz.getPackage().getName();
	}

	/**
	 * Used for testing only
	 */
	private static File toFile(Class<?> clazz) {
		try {
			return new File(clazz.getResource(clazz.getSimpleName() + ".class").toURI());
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}
}