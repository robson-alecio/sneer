package functional.adapters.freedom1;

import org.junit.Ignore;

import functional.SovereignCommunity;
import functional.adapters.SneerCommunity;
import functional.freedom1.Freedom1TestBase;

@Ignore
public class SneerFreedom1Test extends Freedom1TestBase {

	@Override
	protected SovereignCommunity createNewCommunity() {
		return new SneerCommunity(tmpDirectory());
	}
}
