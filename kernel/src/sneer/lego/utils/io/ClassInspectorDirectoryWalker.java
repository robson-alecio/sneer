package sneer.lego.utils.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import sneer.lego.utils.asm.ClassUtils;
import sneer.lego.utils.asm.MetaClass;

public abstract class ClassInspectorDirectoryWalker extends FilteringDirectoryWalker {

	private static final FileFilter FILTER = new OrFileFilter(new SuffixFileFilter(".class"), DirectoryFileFilter.INSTANCE);
	
	public ClassInspectorDirectoryWalker(File root) {
		super(root, FILTER);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
		MetaClass metaClass = ClassUtils.metaClass(file);
		handleClass(metaClass, depth, results);
	}
	
	protected abstract void handleClass(MetaClass metaClass, int depth, Collection<MetaClass> results) 
		throws IOException;

	@SuppressWarnings("unchecked")
	public List<MetaClass> listMetaClasses() {
		List<MetaClass> result = new ArrayList<MetaClass>();
		return (List<MetaClass>) walkAndCollect(result);
	}
}
