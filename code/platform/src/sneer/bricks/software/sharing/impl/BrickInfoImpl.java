package sneer.bricks.software.sharing.impl;

import sneer.bricks.software.sharing.BrickInfo;
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

}
