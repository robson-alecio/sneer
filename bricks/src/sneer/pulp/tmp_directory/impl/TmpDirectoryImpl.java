package sneer.pulp.tmp_directory.impl;

import java.io.File;
import java.io.IOException;

import sneer.pulp.tmp_directory.TmpDirectory;

public class TmpDirectoryImpl implements TmpDirectory {

	public static final String TMP_FILE_PREFIX = "sneer-tmp-";

	@Override
	public File createTempFile(String suffix) throws IOException {
		return File.createTempFile(TMP_FILE_PREFIX, suffix);
	}

	@Override
	public File createTempDirectory(String dirName) throws IOException {
		final File dir = new File(tmpDirectory(), dirName);
		dir.mkdirs();
		return dir;
	}

	private File tmpDirectory() throws IOException {
		return createTempFile(null).getParentFile();
	}
}
