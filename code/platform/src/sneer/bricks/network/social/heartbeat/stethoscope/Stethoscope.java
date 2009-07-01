package sneer.bricks.network.social.heartbeat.stethoscope;

import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface Stethoscope {

	Signal<Boolean> isAlive(Contact contact);
	
}
