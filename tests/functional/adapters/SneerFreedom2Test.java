package functional.adapters;

import functional.Freedom2Test;
import functional.SovereignCommunity;

public class SneerFreedom2Test extends Freedom2Test {

	@Override
	protected SovereignCommunity createNewCommunity() {
	    return new SneerCommunity();
	}

}
