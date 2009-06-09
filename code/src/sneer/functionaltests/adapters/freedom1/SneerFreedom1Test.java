package sneer.functionaltests.adapters.freedom1;

import sneer.functionaltests.SovereignCommunity;
import sneer.functionaltests.adapters.SneerCommunity;
import sneer.functionaltests.freedom1.Freedom1TestBase;

public class SneerFreedom1Test extends Freedom1TestBase {

	@Override
	protected SovereignCommunity createNewCommunity() {
		return new SneerCommunity(tmpDirectory());
	}
}
