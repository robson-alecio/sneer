package sneer.compiler.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

class JavaDirectoryWalker extends DirectoryWalker {
	
	private File _root;
	
	private static final FileFilter FILTER = new OrFileFilter(new SuffixFileFilter(".java"), DirectoryFileFilter.INSTANCE); 
	
	public JavaDirectoryWalker(File root) {
		super(FILTER, -1);
		_root = root;
	}
	
	public List<File> list() throws IOException {
		List<File> result = new ArrayList<File>();
		walk(_root, result);
		return result;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
		results.add(file);
	}
}