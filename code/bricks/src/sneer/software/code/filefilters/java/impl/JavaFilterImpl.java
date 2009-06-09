package sneer.software.code.filefilters.java.impl;

import static sneer.commons.environments.Environments.my;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sneer.software.code.filefilters.java.JavaFilter;
import sneer.software.code.metaclass.MetaClass;
import sneer.software.code.metaclass.MetaClasses;

/**
 * Includes all class files outside hidden directories
 */
class JavaFilterImpl implements JavaFilter {

	private List<MetaClass> _cache;
	private final File _root;

	public JavaFilterImpl(File root) {
		_root = root;
	}

	protected Collection<MetaClass> walkAndCollect(Collection<MetaClass> c) {
			walk(_root, c);
			return c;
	}

	@Override
	public List<File> listFiles() {
		List<File> result = new ArrayList<File>();
		
		for (MetaClass metaClass : listMetaClasses()) 
			result.add(metaClass.classFile());
		
		return result;
	}
	
	@Override
	public List<MetaClass> listMetaClasses() {
		if (_cache == null) 
			_cache = (List<MetaClass>) walkAndCollect(new ArrayList<MetaClass>());
		
		return _cache;
	}
	
	protected final void walk(File startDirectory, Collection<MetaClass> results) {
		if (startDirectory == null) 
			throw new NullPointerException("Start Directory is null");
		
		walk(startDirectory, 0, results);
	}

	private void walk(File directory, int depth, Collection<MetaClass> results) {
		int childDepth = depth + 1;
		File[] childFiles = directory.listFiles();
		
		if (childFiles != null) 
			for (File childFile : childFiles) 
				if (childFile.isDirectory()) 
					walk(childFile, childDepth, results);
				else 
					results.add(my(MetaClasses.class).metaClass(_root, childFile));
	}
}