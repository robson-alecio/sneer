package spikes.sneer.kernel.container.utils.io;

import java.io.File;


/**
 * Includes all java source files outside hidden directories
 */

@SuppressWarnings("deprecation")
public class JavaSourceFilter extends SimpleFilter {
	
	public JavaSourceFilter(File root) {
		super(root, JAVA_SOURCE_FILE_FILTER);
	}
}