package sneer.kernel.container.utils.io;

import java.io.File;

import wheel.io.codegeneration.SimpleFilter;

/**
 * Includes all java source files outside hidden directories
 */
public class JavaSourceFilter extends SimpleFilter {
	
	public JavaSourceFilter(File root) {
		super(root, JAVA_SOURCE_FILE_FILTER);
	}
}