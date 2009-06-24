package sneer.tests.adapters.freedom2;

import sneer.tests.SovereignCommunity;
import sneer.tests.adapters.SneerCommunity;
import sneer.tests.freedom2.Freedom2TestBase;


public class SneerFreedom2Test extends Freedom2TestBase {

	@Override
	protected SovereignCommunity createNewCommunity() {
	    return new SneerCommunity(tmpDirectory());
	}

}
