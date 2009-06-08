package sneer.software.code.filefilters.java.impl;

import static sneer.commons.environments.Environments.my;

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

import sneer.commons.lang.exceptions.NotImplementedYet;
import sneer.hardware.io.IO;
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
	
	public List<File> list() {
		List<File> result = new ArrayList<File>();
		return (List<File>) walkAndCollect(result);
	}
	
	protected <T> Collection<T> walkAndCollect(Collection<T> c) {
		try {
			walk(_root, c);
		} catch (IOException e) {
			throw new NotImplementedYet();
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
		boolean ignore = ignoreDirectory(directory) || my(IO.class).files().isEmpty(directory);
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