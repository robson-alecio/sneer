package sneer.bricks.softwaresharing.impl;

import java.util.List;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.softwaresharing.BrickVersion;
import sneer.bricks.softwaresharing.FileVersion;
import sneer.foundation.lang.exceptions.NotImplementedYet;

class BrickVersionImpl implements BrickVersion {


	private final Sneer1024 _hash;
	private boolean _stagedForExecution;
	
	BrickVersionImpl(Sneer1024 hash) {
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
		return _stagedForExecution;
	}

	@Override
	public int unknownUsers() {
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
	public Status status() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}

	public void setStagedForExecution(boolean value) {
		_stagedForExecution = value;
	}

}
