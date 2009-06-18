package sneer.tests.adapters.freedom7;

import sneer.tests.SovereignCommunity;
import sneer.tests.adapters.SneerCommunity;
import sneer.tests.freedom7.Freedom7TestBase;

public class SneerFreedom7Test extends Freedom7TestBase {

	@Override
	protected SovereignCommunity createNewCommunity() {
		return new SneerCommunity(tmpDirectory());
	}

}
