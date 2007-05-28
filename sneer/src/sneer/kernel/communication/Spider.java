package sneer.kernel.communication;

import sneer.kernel.business.Business;
import wheel.io.network.OldNetwork;
import wheel.reactive.lists.impl.SimpleListReceiver;

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

	private class MyContactReceiver extends SimpleListReceiver {

		@Override
		public void elementAdded(int index) {
			// Implement Auto-generated method stub
			
		}

		@Override
		public void elementRemoved(int index) {
			// Implement Auto-generated method stub
			
		}

	}
	
}
