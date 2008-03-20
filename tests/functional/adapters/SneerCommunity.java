package functional.adapters;

import functional.SovereignCommunity;
import functional.SovereignParty;

public class SneerCommunity implements SovereignCommunity {

	private int _nextPort = 10000;
    
	@Override
	public SovereignParty createParty(String name) {
		return new SneerParty(name, _nextPort++);
	}

}
