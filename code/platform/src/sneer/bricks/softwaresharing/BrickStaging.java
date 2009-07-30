package sneer.bricks.softwaresharing;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.Tuple;

public class BrickStaging extends Tuple {

	public final String brickName;
	public final Sneer1024 hashOfStagedVersion;

	public BrickStaging(String brickName_, Sneer1024 hashOfStagedVersion_) {
		brickName = brickName_;
		hashOfStagedVersion = hashOfStagedVersion_;
	}

	@Override
	public String toString() {
		return "BrickStaging: " + brickName;
	}

}
