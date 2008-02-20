package spikes.lego.app.sample.impl;

import spikes.lego.Brick;
import spikes.lego.app.sample.NameKeeper;
import spikes.lego.store.ObjectStore;

public class NameKeeperImpl implements NameKeeper {

	@Brick
	private ObjectStore _store;

	private String _name;
	
	@Override
	public String sayMyNameBeach() {
		System.out.println(_name);
		return _name;
	}

	@Override
	public void setName(String name) {
		_name = name;
		_store.store(_name);
	}
}
