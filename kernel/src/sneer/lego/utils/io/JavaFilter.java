package sneer.lego.utils.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sneer.lego.utils.asm.ClassUtils;
import sneer.lego.utils.asm.MetaClass;

/**
 * Includes all class files outside hidden directories
 */
public class JavaFilter extends SimpleFilter {

	public JavaFilter(File root) {
		super(root, JAVA_CLASS_FILE_FILTER);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
		MetaClass metaClass = ClassUtils.metaClass(file);
		handleClass(metaClass, depth, results);
	}
	
	@SuppressWarnings("unused")
	protected void handleClass(MetaClass metaClass, int depth, Collection<MetaClass> results) throws IOException {
		results.add(metaClass);
	}

	@SuppressWarnings("unchecked")
	public List<MetaClass> listMetaClasses() {
		List<MetaClass> result = new ArrayList<MetaClass>();
		return (List<MetaClass>) walkAndCollect(result);
	}
}
