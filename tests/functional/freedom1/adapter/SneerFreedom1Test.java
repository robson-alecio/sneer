package functional.freedom1.adapter;

import functional.SovereignCommunity;
import functional.adapters.SneerCommunity;
import functional.freedom1.Freedom1Test;

public class SneerFreedom1Test extends Freedom1Test {

	@Override
	protected SovereignCommunity createNewCommunity() {
		return new SneerCommunity();
	}
}
