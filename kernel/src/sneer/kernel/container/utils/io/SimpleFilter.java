package sneer.kernel.container.utils.io;

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

import sneer.kernel.container.utils.FileUtils;

/**
 * Includes all files outside hidden directories
 */
public class SimpleFilter extends DirectoryWalker {
	
	protected static final FileFilter JAVA_SOURCE_FILE_FILTER = new OrFileFilter(new SuffixFileFilter(".java"),  DirectoryFileFilter.INSTANCE);
	protected static final FileFilter JAVA_CLASS_FILE_FILTER  = new OrFileFilter(new SuffixFileFilter(".class"), DirectoryFileFilter.INSTANCE);
	protected static final FileFilter JAR_FILE_FILTER  = new SuffixFileFilter(".jar");
	
	protected File _root;
	
	private static final String[] IGNORE_DIRECTORY_NAMES = new String[]{"."}; 
	
	public SimpleFilter(File root, FileFilter filter) {
		super(filter, -1);
		_root = root;
	}

	public File root() {
		return _root;
	}
	
	@SuppressWarnings("unchecked")
	public List<File> list() {
		List<File> result = new ArrayList<File>();
		return (List<File>) walkAndCollect(result);
	}
	
	@SuppressWarnings("unchecked")
	protected Collection walkAndCollect(Collection c) {
		try {
			walk(_root, c);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return c;
	}

	@SuppressWarnings({"unchecked", "unused"})
	@Override
	protected void handleFile(File file, int depth, Collection results) throws IOException {
		results.add(file);
	}

	protected String[] ignoreDirectoryNames() {
		return IGNORE_DIRECTORY_NAMES;
	}

	@SuppressWarnings({"unchecked", "unused"})
	@Override
	protected boolean handleDirectory(File directory, int depth, Collection results) throws IOException {
		boolean ignore = ignoreDirectory(directory) || FileUtils.isEmpty(directory);
		return !ignore;
	}

	private boolean ignoreDirectory(File directory) {
		String[] ignored = ignoreDirectoryNames();
		if(ignored == null || ignored.length == 0)
			return false;
		
		String name = directory.getName();
		for (String directoryName : ignored) {
			if(directoryName.equals(name)) {
				return true;
			}
		}
		return false;
	}
}