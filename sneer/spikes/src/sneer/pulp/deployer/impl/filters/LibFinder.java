package sneer.pulp.deployer.impl.filters;

import java.io.File;

import sneer.software.code.filefilters.java.impl.SimpleFilter;


public class LibFinder extends SimpleFilter {

	public LibFinder(File root) {
		super(root, JAR_FILE_FILTER);
	}
}
