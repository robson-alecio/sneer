package sneer.bricks.softwaresharing.impl;

import java.util.List;

import sneer.bricks.softwaresharing.BrickInfo;
import sneer.bricks.softwaresharing.BrickVersion;
import sneer.foundation.lang.exceptions.NotImplementedYet;

class BrickInfoImpl implements BrickInfo {

	private final String _brickName;

	public BrickInfoImpl(String brickName) {
		_brickName = brickName;
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
		throw new NotImplementedYet(); // Implement
	}

}
