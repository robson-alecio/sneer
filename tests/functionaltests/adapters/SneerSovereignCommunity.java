package functionaltests.adapters;

import functionaltests.SovereignCommunity;
import functionaltests.SovereignParty;

public class SneerSovereignCommunity implements SovereignCommunity {

	private int _nextPort = 10000;
    
	@Override
	public SovereignParty createParty(String name) {
		return new SneerSovereignParty(name, _nextPort++);
	}

}
