package sneer.functionaltests.adapters.freedom2;

import org.junit.Ignore;

import sneer.functionaltests.SovereignCommunity;
import sneer.functionaltests.adapters.SneerCommunity;
import sneer.functionaltests.freedom2.Freedom2TestBase;


@Ignore
public class SneerFreedom2Test extends Freedom2TestBase {

	@Override
	protected SovereignCommunity createNewCommunity() {
	    return new SneerCommunity(tmpDirectory());
	}

}
