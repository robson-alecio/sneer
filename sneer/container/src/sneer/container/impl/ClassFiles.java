package sneer.container.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

public class ClassFiles {

	public static File[] list(final String path) throws FileNotFoundException {
		final File directory = new File(path);
		if (!directory.exists())
			throw new FileNotFoundException(path);
		return directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".class");
			}
		});
	}

}
