package sneer.bricks.classpath.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import org.apache.commons.io.filefilter.SuffixFileFilter;

import sneer.lego.utils.FileUtils;

class LibdirClasspath extends ClasspathSupport {

	private File _libdir;
	
	public LibdirClasspath(File folder) {
		_libdir = folder;
	}

	@Override
	public String asJavacArgument() {
		StringBuffer sb = new StringBuffer();
		if(!FileUtils.isEmpty(_libdir)) {
			sb.append(File.pathSeparatorChar);
			File[] libs = _libdir.listFiles((FilenameFilter) new SuffixFileFilter(".jar"));
			for (File lib : libs) {
				sb.append(lib.getAbsolutePath());
				sb.append(File.pathSeparatorChar);
			}
		}
		String result = sb.toString();
		return result;
	}

	@Override
	public <T> List<Class<T>> findAssignableTo(Class<T> clazz) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}
}
