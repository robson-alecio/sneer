package sneer.bricks.softwaresharing.impl;

import java.util.ArrayList;
import java.util.List;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.softwaresharing.BrickInfo;
import sneer.bricks.softwaresharing.BrickVersion;
import sneer.foundation.lang.exceptions.NotImplementedYet;

class BrickInfoImpl implements BrickInfo {

	private final String _brickName;
	private final Sneer1024 _hash;

	public BrickInfoImpl(String brickName, Sneer1024 hash) {
		_brickName = brickName;
		_hash = hash;
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
		List<BrickVersion> result = new ArrayList<BrickVersion>();
		result.add(new BrickVersionImpl(_brickName, _hash));
		return result;
	}

	@Override
	public Status status() {
		throw new NotImplementedYet(); // Implement
	}

}
