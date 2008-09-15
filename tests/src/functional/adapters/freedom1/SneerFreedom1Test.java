package functional.adapters.freedom1;

import functional.SovereignCommunity;
import functional.adapters.SneerCommunity;
import functional.freedom1.Freedom1TestBase;

public class SneerFreedom1Test extends Freedom1TestBase {

	@Override
	protected SovereignCommunity createNewCommunity() {
		return new SneerCommunity(tmpDirectory());
	}
}
