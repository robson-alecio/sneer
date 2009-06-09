package functionaltests.adapters.freedom7;

import functionaltests.SovereignCommunity;
import functionaltests.adapters.SneerCommunity;
import functionaltests.freedom7.Freedom7TestBase;

public class SneerFreedom7Test extends Freedom7TestBase {

	@Override
	protected SovereignCommunity createNewCommunity() {
		return new SneerCommunity(tmpDirectory());
	}

}
