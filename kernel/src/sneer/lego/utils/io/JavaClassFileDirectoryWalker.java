package sneer.lego.utils.io;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;


public class JavaClassFileDirectoryWalker extends FilteringDirectoryWalker {
	
	private static final FileFilter FILTER = new OrFileFilter(new SuffixFileFilter(".class"), DirectoryFileFilter.INSTANCE); 
	
	public JavaClassFileDirectoryWalker(File root) {
		super(root, FILTER);
	}
}