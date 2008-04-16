package sneer.lego.utils.asm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;

public class ClassUtils {

	public static MetaClass metaClass(File classFile) throws IOException {
		ClassReader reader = new ClassReader(new FileInputStream(classFile));
		MetaClass visitor = new MetaClass(classFile);
		reader.accept(visitor, 0);
		return visitor;
	}
}