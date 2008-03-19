package functionaltests.adapters;

import functionaltests.SovereignCommunity;
import functionaltests.SovereignParty;

public class SneerCommunity implements SovereignCommunity {

	private int _nextPort = 10000;
    
	@Override
	public SovereignParty createParty(String name) {
		return new SneerParty(name, _nextPort++);
	}

}
