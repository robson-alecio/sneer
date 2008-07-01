package sneerapps.giventake.tests.impl;

import java.util.Collection;

import sneer.bricks.things.Thing;
import sneer.bricks.things.ThingHome;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneerapps.giventake.GiveNTake;
import sneerapps.giventake.tests.GiveNTakeUser;

class GiveNTakeUserImpl implements GiveNTakeUser {

	private final Container _container = ContainerUtils.newContainer();
	
	public void advertise(String title, String description) {
		GiveNTake gnt = _container.produce(GiveNTake.class);
		ThingHome thingHome = _container.produce(ThingHome.class);
		
		gnt.advertise(thingHome.create(title, description));
	}

	public Collection<Thing> search(String tags) {
		tags.toString();
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

}
