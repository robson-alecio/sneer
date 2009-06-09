package functionaltests.adapters.freedom2;

import org.junit.Ignore;

import functionaltests.SovereignCommunity;
import functionaltests.adapters.SneerCommunity;
import functionaltests.freedom2.Freedom2TestBase;

@Ignore
public class SneerFreedom2Test extends Freedom2TestBase {

	@Override
	protected SovereignCommunity createNewCommunity() {
	    return new SneerCommunity(tmpDirectory());
	}

}
