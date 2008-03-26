package functional.adapters;

import sneer.bricks.network.Network;
import functional.SovereignCommunity;
import functional.SovereignParty;

public class SneerCommunity implements SovereignCommunity {

	private int _nextPort = 10000;

	private Network _network;
	
	public SneerCommunity(Network network) {
		_network = network;
	}

	@Override
	public SovereignParty createParty(String name) {
		return new SneerParty(name, _nextPort++, _network);
	}

}
