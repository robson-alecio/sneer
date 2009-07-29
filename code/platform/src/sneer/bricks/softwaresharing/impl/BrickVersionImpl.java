package sneer.bricks.softwaresharing.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.List;

import sneer.bricks.hardwaresharing.files.FileSpace;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.softwaresharing.BrickVersion;
import sneer.bricks.softwaresharing.FileVersion;
import sneer.foundation.lang.exceptions.NotImplementedYet;

class BrickVersionImpl implements BrickVersion {

	private final String _brickName;
	private final Sneer1024 _hash;

	public BrickVersionImpl(String brickName, Sneer1024 hash) {
		_brickName = brickName;
		_hash = hash;
	}

	@Override
	public List<FileVersion> files() {
		try {
			File tmpFolder = File.createTempFile("tmpSrcForBrick_" + _brickName + "_", "");
			my(FileSpace.class).fetchContentsInto(tmpFolder, System.currentTimeMillis(), _hash);
		} catch (IOException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
		
		//List<FileVersion> result = new ArrayList<FileVersion>();
		//result.add(new FileVersionImpl());
		//return result;
		
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
	public void setStagedForExecution(boolean staged) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public File sourceFolder() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
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
