package build.antFileGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wheel.io.files.Directory;
import build.dotClasspathParser.DotClasspath;


public class DotClasspathToAntConverter {

	private final AntFileBuilder _antFileBuilder;

	public DotClasspathToAntConverter(final AntFileBuilder antFileBuilder) {
		_antFileBuilder = antFileBuilder;
	}
	
	public DotClasspathToAntConverter(final Directory directory) {
		_antFileBuilder = new AntFileBuilderToFilesystem(directory);
	}

	public void createAntFile(final DotClasspath classpath){
		for (final String lib : reversedCopy(classpath.getLibs()))
			_antFileBuilder.addClasspathEntry(lib);
		
		for (final DotClasspath.Entry entry: reversedCopy(classpath.getSrcs()))
			_antFileBuilder.addCompileEntry(entry._src, entry._output);
		
		_antFileBuilder.build();
	}

	private <T> ArrayList<T> reversedCopy(final List<T> collection) {
		final ArrayList<T> libsReversed = new ArrayList<T>(collection);
		Collections.reverse(libsReversed);
		return libsReversed;
	}

}
