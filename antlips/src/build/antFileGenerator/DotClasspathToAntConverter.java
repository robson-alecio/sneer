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
		
		for (final String src : reversedCopy(classpath.getSrcs()))
			_antFileBuilder.addCompileEntry(src);
		
		_antFileBuilder.build();
	}

	private ArrayList<String> reversedCopy(final List<String> collection) {
		final ArrayList<String> libsReversed = new ArrayList<String>(collection);
		Collections.reverse(libsReversed);
		return libsReversed;
	}

}
