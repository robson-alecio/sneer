package sneer.bricks.software.code.filefilters.java.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sneer.bricks.software.code.filefilters.java.JavaFilter;
import sneer.bricks.software.code.metaclass.MetaClass;
import sneer.bricks.software.code.metaclass.MetaClasses;

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
	
	protected final void walk(File startFolder, Collection<MetaClass> results) {
		if (startFolder == null) 
			throw new NullPointerException("Start Folder is null");
		
		walk(startFolder, 0, results);
	}

	private void walk(File folder, int depth, Collection<MetaClass> results) {
		File[] childFiles = folder.listFiles();
		if (childFiles == null) 
			return;
		
		int childDepth = depth + 1;
		for (File childFile : childFiles) 
			if (childFile.isDirectory()) walk(childFile, childDepth, results);
			else results.add(my(MetaClasses.class).metaClass(_root, childFile));
	}
}