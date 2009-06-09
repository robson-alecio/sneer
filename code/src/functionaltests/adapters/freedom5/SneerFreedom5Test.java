package functionaltests.adapters.freedom5;

import functionaltests.SovereignCommunity;
import functionaltests.adapters.SneerCommunity;
import functionaltests.freedom5.Freedom5TestBase;

public class SneerFreedom5Test extends Freedom5TestBase {

	@Override
	protected SovereignCommunity createNewCommunity() {
	    return new SneerCommunity(tmpDirectory());
	}

}