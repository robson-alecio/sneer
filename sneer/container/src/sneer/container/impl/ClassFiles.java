package sneer.container.impl;

import java.io.File;
import java.io.FilenameFilter;

public class ClassFiles {

	public static File[] list(final String directory) {
		return new File(directory).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".class");
			}
		});
	}

}
