package functionaltests.adapters.freedom1;

import functionaltests.SovereignCommunity;
import functionaltests.adapters.SneerCommunity;
import functionaltests.freedom1.Freedom1TestBase;

public class SneerFreedom1Test extends Freedom1TestBase {

	@Override
	protected SovereignCommunity createNewCommunity() {
		return new SneerCommunity(tmpDirectory());
	}
}
