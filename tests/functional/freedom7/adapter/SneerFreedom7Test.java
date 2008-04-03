package functional.freedom7.adapter;

import functional.SovereignCommunity;
import functional.SovereignParty;
import functional.adapters.SneerCommunity;
import functional.freedom7.BrickPublisher;
import functional.freedom7.Freedom7Test;


public class SneerFreedom7Test extends Freedom7Test {

	@Override
	protected BrickPublisher wrapParty(SovereignParty party) {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	protected SovereignCommunity createNewCommunity() {
		return new SneerCommunity();
	}
}
