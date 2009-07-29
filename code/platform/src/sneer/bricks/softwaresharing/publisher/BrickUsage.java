package sneer.bricks.softwaresharing.publisher;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.Tuple;

public class BrickUsage extends Tuple {

	public final String brickName;
	public final Sneer1024 hashOfCurrentVersion;

	public BrickUsage(String brickName_, Sneer1024 hashOfCurrentVersion_) {
		brickName = brickName_;
		hashOfCurrentVersion = hashOfCurrentVersion_;
	}

	@Override
	public String toString() {
		return "BrickUsage: " + brickName;
	}

}
