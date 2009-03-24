package functional.adapters.freedom7;

import org.junit.Ignore;

import functional.SovereignCommunity;
import functional.adapters.SneerCommunity;
import functional.freedom7.Freedom7TestBase;

@Ignore
public class SneerFreedom7Test extends Freedom7TestBase {

	@Override
	protected SovereignCommunity createNewCommunity() {
		return new SneerCommunity(tmpDirectory());
	}

}
