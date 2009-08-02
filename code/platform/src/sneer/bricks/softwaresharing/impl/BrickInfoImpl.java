package sneer.bricks.softwaresharing.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import sneer.bricks.hardwaresharing.files.client.FileClient;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.bricks.softwaresharing.BrickInfo;
import sneer.bricks.softwaresharing.BrickStaging;
import sneer.bricks.softwaresharing.BrickVersion;
import sneer.foundation.lang.exceptions.NotImplementedYet;

class BrickInfoImpl implements BrickInfo {

	private final String _brickName;
	
	private final Sneer1024 _hashOfCurrentVersion;
	private final BrickVersion _currentVersion;

	
	public BrickInfoImpl(String brickName, Sneer1024 hash) {
		_brickName = brickName;
		
		_hashOfCurrentVersion = hash;
		_currentVersion = fetchSingleVersion();
	}


	private BrickVersion fetchSingleVersion() {
		try {
			return tryToFetchSingleVersion();
		} catch (IOException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}


	private BrickVersion tryToFetchSingleVersion() throws IOException {
		File srcFolder = File.createTempFile("tmpSrcForBrick_" + _brickName + "_", "");
		srcFolder.delete();
		fetchInto(srcFolder);
		return new BrickVersionImpl(srcFolder, _hashOfCurrentVersion);
	}


	private void fetchInto(File folder) throws IOException {
		my(FileClient.class).fetchContentsInto(folder, System.currentTimeMillis(), _hashOfCurrentVersion);
	}

	@Override
	public boolean isSnapp() {
		throw new NotImplementedYet(); // Implement
	}

	@Override
	public String name() {
		return _brickName;
	}
	

	@Override
	public List<BrickVersion> versions() {
		return Arrays.asList(_currentVersion);
	}

	@Override
	public Status status() {
		throw new NotImplementedYet(); // Implement
	}


	@Override
	public void setStagedForExecution(BrickVersion version, boolean staged) {
		my(TupleSpace.class).publish(new BrickStaging(_brickName, version.hash()));
	}

}
