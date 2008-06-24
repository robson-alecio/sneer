package sneer.bricks.things.impl;

import java.util.ArrayList;
import java.util.Collection;

import sneer.bricks.things.Thing;
import sneer.bricks.things.ThingHome;

public class ThingHomeImpl implements ThingHome {

	private Collection<Thing> _all = new ArrayList<Thing>();

	@Override
	public Collection<Thing> find(String tags) {
		return new Finder().find(_all, tags); //Optimize
	}

	@Override
	public Thing create(String name, String description) {
		ThingImpl result = new ThingImpl(name, description);
		_all.add(result);
		return result;
	}

}
