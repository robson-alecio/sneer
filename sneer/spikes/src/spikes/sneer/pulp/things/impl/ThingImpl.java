package spikes.sneer.pulp.things.impl;

import spikes.sneer.pulp.things.Thing;

class ThingImpl implements Thing {

	private final String _name;
	private final String _description;

	public ThingImpl(String name, String description) {
		_name = name;
		_description = description;
	}

	public String name() {
		return _name;
	}

	public String description() {
		return _description;
	}

	
	
}
