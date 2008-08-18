package sneer.lego.utils.metaclass;

import java.io.File;

import org.apache.commons.lang.SystemUtils;

public class ClassUtils {

    private static final File _defaultRootDirectoryUnderEclipse = new File(SystemUtils.getUserDir(), "bin");
    
	public static MetaClass metaClass(File rootDirectory, File classFile) {
	    return new SimpleMetaClass(rootDirectory, classFile);
		//return new LazyMetaClass(rootDirectory, classFile);
	}

	public static MetaClass metaClass(Class<?> clazz) {
	    return ClassUtils.metaClass(_defaultRootDirectoryUnderEclipse, toFile(clazz));
	}

	/**
	 * Used for testing only
	 */
	public static File toFile(Class<?> clazz) {
		return toFile(clazz.getName());
	}

	private static File toFile(String clazz) {
		File classFile = new File(_defaultRootDirectoryUnderEclipse, clazz.replaceAll("\\.", "/") + ".class");
		return classFile;
	}
}