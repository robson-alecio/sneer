package functionaltests.impl;

import functionaltests.SovereignCommunity;
import functionaltests.SovereignParty;
import functionaltests.impl.mock.SovereignPartyMock;

public class SovereignCommunityImpl implements SovereignCommunity {

	@Override
	public SovereignParty createParty(String name) {
		return new SovereignPartyMock(name);
	}

}
