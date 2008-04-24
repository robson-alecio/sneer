package sneer.lego.utils.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sneer.lego.utils.asm.ClassUtils;
import sneer.lego.utils.asm.IMetaClass;

/**
 * Includes all class files outside hidden directories
 */
public class JavaFilter extends SimpleFilter {

	private List<IMetaClass> _cache;
	
	public JavaFilter(File root) {
		super(root, JAVA_CLASS_FILE_FILTER);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
		IMetaClass metaClass = ClassUtils.metaClass(_root, file);
		handleClass(metaClass, depth, results);
	}
	
	@SuppressWarnings("unused")
	protected void handleClass(IMetaClass metaClass, int depth, Collection<IMetaClass> results) throws IOException {
		results.add(metaClass);
	}

	@SuppressWarnings("unchecked")
	public List<IMetaClass> listMetaClasses() {
		if(_cache != null) {
			return _cache;
		}
		_cache = (List<IMetaClass>) walkAndCollect(new ArrayList<IMetaClass>());
		return _cache; 
	}

	public List<File> listFiles() {
		List<File> result = new ArrayList<File>();
		List<IMetaClass> metaClasses = listMetaClasses();
		for (IMetaClass metaClass : metaClasses) {
			result.add(metaClass.classFile());
		}
		return result;
	}

}
