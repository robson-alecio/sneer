package sneer.bricks.software.sharing.publisher;

import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.brickness.Tuple;

public class BrickUsage extends Tuple {

	public final String brickName;
	public final Sneer1024 hash;

	public BrickUsage(String brickName_, Sneer1024 hash_) {
		brickName = brickName_;
		hash = hash_;
	}

	@Override
	public String toString() {
		return "BrickUsage: " + brickName;
	}

	public Sneer1024 hash() {
		return hash;
	}
	
}
