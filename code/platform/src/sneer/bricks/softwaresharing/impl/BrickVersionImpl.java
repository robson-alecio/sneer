package sneer.bricks.softwaresharing.impl;

import java.io.File;
import java.util.List;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.softwaresharing.BrickVersion;
import sneer.bricks.softwaresharing.FileVersion;
import sneer.foundation.lang.exceptions.NotImplementedYet;

class BrickVersionImpl implements BrickVersion {


	private File _sourceFolder;

	public BrickVersionImpl(File sourceFolder) {
		_sourceFolder = sourceFolder;
	}

	@Override
	public List<FileVersion> files() {
		throw new NotImplementedYet();
	}

	@Override
	public Sneer1024 hash() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public boolean isStagedForExecution() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public List<String> knownUsers() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public long publicationDate() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void setRejected(boolean rejected) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void setStagedForExecution(boolean staged) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public File sourceFolder() {
		return _sourceFolder;
	}

	@Override
	public Status status() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public int unknownUsers() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}

}
