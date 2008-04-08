package sneer.bricks.compiler.tests;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.commons.io.filefilter.SuffixFileFilter;

import sneer.bricks.compiler.Classpath;

public class LibdirClasspath implements Classpath {

	private File _libdir;
	
	public LibdirClasspath(File folder) {
		_libdir = folder;
	}

	public void x() {
		StringBuffer sb = new StringBuffer();
		if(!sneer.lego.utils.FileUtils.isEmpty(_libdir)) {
			sb.append(File.pathSeparatorChar);
			File[] libs = _libdir.listFiles((FilenameFilter) new SuffixFileFilter(".jar"));
			for (File lib : libs) {
				sb.append(lib.getAbsolutePath());
				sb.append(File.pathSeparatorChar);
			}
		}
		String classPath = sb.toString(); 
		System.out.println(classPath);
	}
}
