package functional.adapters.freedom2;

import functional.SovereignCommunity;
import functional.adapters.SneerCommunity;
import functional.freedom2.Freedom2Test;

public class SneerFreedom2Test extends Freedom2Test {

	@Override
	protected SovereignCommunity createNewCommunity() {
	    return new SneerCommunity(tmpDirectory());
	}

}
