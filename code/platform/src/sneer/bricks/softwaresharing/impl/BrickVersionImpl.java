package sneer.bricks.softwaresharing.impl;

import java.io.File;
import java.util.List;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.softwaresharing.BrickVersion;
import sneer.bricks.softwaresharing.FileVersion;
import sneer.foundation.lang.exceptions.NotImplementedYet;

class BrickVersionImpl implements BrickVersion {


	private File _sourceFolder;
	private final Sneer1024 _hash;

	
	BrickVersionImpl(File sourceFolder, Sneer1024 hash) {
		_sourceFolder = sourceFolder;
		_hash = hash;
	}

	@Override
	public List<FileVersion> files() {
		throw new NotImplementedYet();
	}

	@Override
	public Sneer1024 hash() {
		return _hash;
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
