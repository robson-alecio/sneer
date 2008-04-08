package sneer.bricks.compiler.impl;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import sneer.lego.utils.io.FilteringDirectoryWalker;

class JavaDirectoryWalker extends FilteringDirectoryWalker {
	
	private static final FileFilter FILTER = new OrFileFilter(new SuffixFileFilter(".java"), DirectoryFileFilter.INSTANCE); 
	
	public JavaDirectoryWalker(File root) {
		super(root, FILTER);
	}
}