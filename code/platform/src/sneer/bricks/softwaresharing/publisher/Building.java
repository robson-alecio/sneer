package sneer.bricks.softwaresharing.publisher;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.Tuple;

/** Conveys the hash of all bricks of a given publisher at a certain point in time. */
public class Building extends Tuple {

	public final Sneer1024 hashOfAllBricks;

	public Building(Sneer1024 hashOfAllBricks_) {
		hashOfAllBricks = hashOfAllBricks_;
	}

}
