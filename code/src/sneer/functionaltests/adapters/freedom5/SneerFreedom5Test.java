package sneer.functionaltests.adapters.freedom5;

import sneer.functionaltests.SovereignCommunity;
import sneer.functionaltests.adapters.SneerCommunity;
import sneer.functionaltests.freedom5.Freedom5TestBase;

public class SneerFreedom5Test extends Freedom5TestBase {

	@Override
	protected SovereignCommunity createNewCommunity() {
	    return new SneerCommunity(tmpDirectory());
	}

}