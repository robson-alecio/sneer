package sneer.tests.adapters.freedom1;

import sneer.tests.SovereignCommunity;
import sneer.tests.adapters.SneerCommunity;
import sneer.tests.freedom1.Freedom1TestBase;

public class SneerFreedom1Test extends Freedom1TestBase {

	@Override
	protected SovereignCommunity createNewCommunity() {
		return new SneerCommunity(tmpDirectory());
	}
}
