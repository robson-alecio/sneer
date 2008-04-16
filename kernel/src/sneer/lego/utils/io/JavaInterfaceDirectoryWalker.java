package sneer.lego.utils.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import sneer.lego.utils.FileUtils;
import sneer.lego.utils.asm.ClassUtils;
import sneer.lego.utils.asm.MetaClass;


public class JavaInterfaceDirectoryWalker extends FilteringDirectoryWalker {
	
	private static final FileFilter FILTER = new OrFileFilter(new SuffixFileFilter(".class"), DirectoryFileFilter.INSTANCE); 
	
	public JavaInterfaceDirectoryWalker(File root) {
		super(root, FILTER);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
		MetaClass metaClass = ClassUtils.metaClass(file);
		if(metaClass.isInterface())
			results.add(file);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	protected boolean handleDirectory(File directory, int depth, Collection results) throws IOException {
		String name = directory.getName();
		boolean skip = name.startsWith(".") || "impl".equals(name) || FileUtils.isEmpty(directory);
		if(skip) return false;
		return true;
	}
}