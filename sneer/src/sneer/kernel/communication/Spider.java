package sneer.kernel.communication;

import sneer.kernel.business.Business;
import wheel.io.network.OldNetwork;

class Spider {

	static void start(OldNetwork network, Business business) {
		new Spider(network, business);
	}
	
	private Spider(OldNetwork network, Business business) {
		_network = network;
		_business = business;
	}
	
	private final OldNetwork _network;
	private final Business _business;
	
}
