package sneer.functionaltests.adapters.freedom7;

import sneer.functionaltests.SovereignCommunity;
import sneer.functionaltests.adapters.SneerCommunity;
import sneer.functionaltests.freedom7.Freedom7TestBase;

public class SneerFreedom7Test extends Freedom7TestBase {

	@Override
	protected SovereignCommunity createNewCommunity() {
		return new SneerCommunity(tmpDirectory());
	}

}
