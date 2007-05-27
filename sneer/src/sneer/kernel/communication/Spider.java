package sneer.kernel.communication;

import sneer.kernel.business.Business;
import wheel.io.network.OldNetwork;
import wheel.reactive.Receiver;
import wheel.reactive.lists.ListValueChange;

class Spider {

	static void start(OldNetwork network, Business business) {
		new Spider(network, business);
	}
	
	private Spider(OldNetwork network, Business business) {
		_network = network;
		_business = business;
		_business.contacts().addListReceiver(new MyContactReceiver());
	}
	
	private final OldNetwork _network;
	private final Business _business;

	private class MyContactReceiver implements Receiver<ListValueChange> {

		@Override
		public void receive(ListValueChange valueChange) {
			// Implement Auto-generated method stub

		}

	}
	
}
