package sneer.bricks.network.social.heartbeat.stethoscope;

import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.software.bricks.snappstarter.Snapp;
import sneer.foundation.brickness.Brick;

@Snapp
@Brick
public interface Stethoscope {

	Signal<Boolean> isAlive(Contact contact);
	
}
