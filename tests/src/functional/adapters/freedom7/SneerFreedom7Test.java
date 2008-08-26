package functional.adapters.freedom7;

import functional.SovereignCommunity;
import functional.adapters.SneerCommunity;
import functional.freedom7.Freedom7Test;


public class SneerFreedom7Test extends Freedom7Test {

	@Override
	protected SovereignCommunity createNewCommunity() {
		return new SneerCommunity();
	}

}