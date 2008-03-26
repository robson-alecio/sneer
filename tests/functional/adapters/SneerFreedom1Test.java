package functional.adapters;

import functional.Freedom1Test;
import functional.SovereignCommunity;

public class SneerFreedom1Test extends Freedom1Test {

	@Override
	protected SovereignCommunity createNewCommunity() {
		return new SneerCommunity();
	}
}
