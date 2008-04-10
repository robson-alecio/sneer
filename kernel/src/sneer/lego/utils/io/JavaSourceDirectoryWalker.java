package sneer.lego.utils.io;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;


public class JavaSourceDirectoryWalker extends FilteringDirectoryWalker {
	
	private static final FileFilter FILTER = new OrFileFilter(new SuffixFileFilter(".java"), DirectoryFileFilter.INSTANCE); 
	
	public JavaSourceDirectoryWalker(File root) {
		super(root, FILTER);
	}
}