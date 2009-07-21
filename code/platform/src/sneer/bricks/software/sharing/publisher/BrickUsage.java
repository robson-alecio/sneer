package sneer.bricks.software.sharing.publisher;

import sneer.foundation.brickness.Tuple;

public class BrickUsage extends Tuple {

	public final String brickName;

	public BrickUsage(String brickName_) {
		brickName = brickName_;
	}

	@Override
	public String toString() {
		return "BrickUsage: " + brickName;
	}

	
	
}
