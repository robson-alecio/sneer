package sneer.pulp.deployer.impl.filters;

import java.io.File;

import wheel.io.codegeneration.SimpleFilter;

public class LibFinder extends SimpleFilter {

	public LibFinder(File root) {
		super(root, JAR_FILE_FILTER);
	}
}
