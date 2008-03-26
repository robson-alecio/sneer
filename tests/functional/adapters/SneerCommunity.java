package functional.adapters;

import sneer.bricks.connection.impl.mock.InMemoryNetwork;
import sneer.bricks.network.Network;
import functional.SovereignCommunity;
import functional.SovereignParty;

public class SneerCommunity implements SovereignCommunity {

	private int _nextPort = 10000;

	private static final Network NETWORK = new InMemoryNetwork();
	
	@Override
	public SovereignParty createParty(String name) {
		return new SneerParty(name, _nextPort++, NETWORK);
	}

}
