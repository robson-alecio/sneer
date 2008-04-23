package sneer.lego.utils.asm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.SystemUtils;
import org.objectweb.asm.ClassReader;

public class ClassUtils {

	public static MetaClass metaClass(File classFile) throws IOException {
		InputStream is = new FileInputStream(classFile);
		ClassReader reader = new ClassReader(is);
		MetaClass visitor = new MetaClass(classFile);
		reader.accept(visitor, 0);
		is.close();
		return visitor;
	}

	public static MetaClass metaClass(Class<?> clazz) throws IOException {
		return ClassUtils.metaClass(toFile(clazz));
	}
	
	/**
	 * Used for testing only
	 */
	public static File toFile(Class<?> clazz) {
		File classFile = new File(SystemUtils.getUserDir(), "bin/"+clazz.getName().replaceAll("\\.", "/") + ".class");
		return classFile;
	}

}