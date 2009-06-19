package sneer.tests.adapters.freedom5;

import sneer.tests.SovereignCommunity;
import sneer.tests.adapters.SneerCommunity;
import sneer.tests.freedom5.Freedom5TestBase;

public class SneerFreedom5Test extends Freedom5TestBase {

	@Override
	protected SovereignCommunity createNewCommunity() {
	    return new SneerCommunity(tmpDirectory());
	}

}