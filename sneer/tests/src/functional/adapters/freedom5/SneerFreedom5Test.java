package functional.adapters.freedom5;

import org.junit.Ignore;

import functional.SovereignCommunity;
import functional.adapters.SneerCommunity;
import functional.freedom5.Freedom5TestBase;

@Ignore
public class SneerFreedom5Test extends Freedom5TestBase {

	@Override
	protected SovereignCommunity createNewCommunity() {
	    return new SneerCommunity(tmpDirectory());
	}

}