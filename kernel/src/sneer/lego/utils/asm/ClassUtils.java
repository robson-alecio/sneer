package sneer.lego.utils.asm;

import java.io.File;

import org.apache.commons.lang.SystemUtils;

public class ClassUtils {

    private static final File _defaultRootDirectoryUnderEclipse = new File(SystemUtils.getUserDir(), "bin");
    
	public static MetaClass metaClass(File rootDirectory, File classFile) {
	    return new LazyMetaClass(rootDirectory, classFile);
	}

	public static MetaClass metaClass(Class<?> clazz) {
		
	    return ClassUtils.metaClass(_defaultRootDirectoryUnderEclipse, toFile(clazz));
	}
	
	/**
	 * Used for testing only
	 */
	public static File toFile(Class<?> clazz) {
		File classFile = new File(_defaultRootDirectoryUnderEclipse, clazz.getName().replaceAll("\\.", "/") + ".class");
		return classFile;
	}

}