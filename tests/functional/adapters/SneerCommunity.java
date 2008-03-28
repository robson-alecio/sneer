package functional.adapters;

import sneer.bricks.network.Network;
import sneer.bricks.network.impl.inmemory.InMemoryNetwork;
import functional.SovereignCommunity;
import functional.SovereignParty;

public class SneerCommunity implements SovereignCommunity {

	private int _nextPort = 10000;

	private final Network _network = new InMemoryNetwork();
	
	@Override
	public SovereignParty createParty(String name) {
		return new SneerParty(name, _nextPort++, _network);
	}

}
