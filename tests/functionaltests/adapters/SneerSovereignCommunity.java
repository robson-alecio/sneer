package functionaltests.adapters;

import functionaltests.SovereignCommunity;
import functionaltests.SovereignParty;

public class SneerSovereignCommunity implements SovereignCommunity {

	@Override
	public SovereignParty createParty(String name) {
		return new SneerSovereignParty(name);
	}
}
