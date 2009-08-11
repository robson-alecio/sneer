package sneer.bricks.softwaresharing.impl;

import java.util.Arrays;
import java.util.List;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.softwaresharing.BrickInfo;
import sneer.bricks.softwaresharing.BrickVersion;
import sneer.foundation.lang.exceptions.NotImplementedYet;

class BrickInfoImpl implements BrickInfo {

	
	private final String _brickName;
	private final BrickVersion _currentVersion;

	
	public BrickInfoImpl(String brickName, Sneer1024 hashOfCurrentVersion) {
		_brickName = brickName;
		_currentVersion = new BrickVersionImpl(hashOfCurrentVersion);
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
		if (!versions().contains(version))
			throw new IllegalArgumentException();
		
		((BrickVersionImpl)version).setStagedForExecution(staged);
	}

}
