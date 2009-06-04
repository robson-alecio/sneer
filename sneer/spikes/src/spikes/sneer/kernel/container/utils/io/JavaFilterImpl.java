package spikes.sneer.kernel.container.utils.io;

import static sneer.commons.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sneer.software.code.filefilters.java.JavaFilter;
import sneer.software.code.metaclass.MetaClass;
import sneer.software.code.metaclass.MetaClasses;

/**
 * Includes all class files outside hidden directories
 */
@Deprecated // This is a copy of the class sneer.software.code.filefilters.java.impl.JavaFilterImpl
class JavaFilterImpl extends SimpleFilter implements JavaFilter {

	private List<MetaClass> _cache;

	public JavaFilterImpl(File root) {
		super(root, JAVA_CLASS_FILE_FILTER);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
		MetaClass metaClass = my(MetaClasses.class).metaClass(_root, file);
		handleClass(metaClass, depth, results);
	}
	
	@SuppressWarnings("unused")
	protected void handleClass(MetaClass metaClass, int depth, Collection<MetaClass> results) throws IOException {
		results.add(metaClass);
	}

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
