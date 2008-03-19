package functionaltests.adapters;

import functionaltests.Freedom2Test;
import functionaltests.SovereignCommunity;

public class SneerFreedom2Test extends Freedom2Test {

	@Override
	protected SovereignCommunity createNewCommunity() {
	    return new SneerCommunity();
	}

}
