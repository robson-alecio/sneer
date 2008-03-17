package spikes.legobricks.name.impl;

import spikes.legobricks.name.OwnNameKeeper;

public class OwnNameKeeperImpl implements OwnNameKeeper {

	private String _name;
	
	@Override
	public String getName() {
		return _name;
	}

	@Override
	public void setName(String name) {
		_name = name;
	}
}
