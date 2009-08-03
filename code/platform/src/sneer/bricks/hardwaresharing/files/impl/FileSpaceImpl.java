package sneer.bricks.hardwaresharing.files.impl;

import java.io.File;
import java.io.IOException;

import sneer.bricks.hardwaresharing.files.FileSpace;
import sneer.bricks.pulp.crypto.Sneer1024;

public class FileSpaceImpl implements FileSpace {

	
	{
		startPrefetchingTuples();
	}
	
	
	@Override
	public Sneer1024 publishContents(File fileOrFolder) throws IOException {
		return Publisher.publishContents(fileOrFolder);
	}

	@Override
	public void fetchContentsInto(File fileOrFolder, long lastModified, Sneer1024 hash) throws IOException {
		Fetcher.fetchContentsInto(fileOrFolder, lastModified, hash);
	}


	private void startPrefetchingTuples() {
		try {
			Class.forName(Fetcher.class.getName()); //Causes Fetcher class to be initialized.
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}
	
}