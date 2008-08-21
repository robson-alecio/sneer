package sneer.kernel.container.utils.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sneer.kernel.container.utils.metaclass.ClassUtils;
import sneer.kernel.container.utils.metaclass.MetaClass;

/**
 * Includes all class files outside hidden directories
 */
public class JavaFilter extends SimpleFilter {

	private List<MetaClass> _cache;
	
	public JavaFilter(File root) {
		super(root, JAVA_CLASS_FILE_FILTER);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
		MetaClass metaClass = ClassUtils.metaClass(_root, file);
		handleClass(metaClass, depth, results);
	}
	
	@SuppressWarnings("unused")
	protected void handleClass(MetaClass metaClass, int depth, Collection<MetaClass> results) throws IOException {
		results.add(metaClass);
	}

	@SuppressWarnings("unchecked")
	public List<MetaClass> listMetaClasses() {
		if(_cache != null) {
			return _cache;
		}
		_cache = (List<MetaClass>) walkAndCollect(new ArrayList<MetaClass>());
		return _cache; 
	}

	public List<File> listFiles() {
		List<File> result = new ArrayList<File>();
		List<MetaClass> metaClasses = listMetaClasses();
		for (MetaClass metaClass : metaClasses) {
			result.add(metaClass.classFile());
		}
		return result;
	}

}
