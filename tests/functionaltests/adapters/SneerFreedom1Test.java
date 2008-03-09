package functionaltests.adapters;

import functionaltests.Freedom1Test;
import functionaltests.SovereignCommunity;

public class SneerFreedom1Test extends Freedom1Test {

	@Override
	protected SovereignCommunity createNewCommunity() {
		return new SneerSovereignCommunity();
	}

}
