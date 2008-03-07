package spikes.legobricks.name.impl;

import sneer.lego.Brick;
import spikes.legobricks.name.NameKeeper;
import spikes.legobricks.store.ObjectStore;

public class NameKeeperImpl implements NameKeeper {

	@Brick
	private ObjectStore _store;

	private String _name;
	
	@Override
	public String getName() {
		return _name;
	}

	@Override
	public void setName(String name) {
		_store.store(_name);
		_name = name;
	}
}
