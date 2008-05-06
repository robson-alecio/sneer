package sneer.bricks.deployer.impl.filters;

import java.io.File;

import sneer.lego.utils.io.SimpleFilter;

public class LibFinder extends SimpleFilter {

	public LibFinder(File root) {
		super(root, JAR_FILE_FILTER);
	}
}
