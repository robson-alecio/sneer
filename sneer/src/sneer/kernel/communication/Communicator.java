package sneer.kernel.communication;


import sneer.kernel.business.Business;
import sneer.kernel.business.BusinessSource;
import wheel.io.network.OldNetwork;
import wheel.io.ui.User;
import wheel.lang.Threads;
import wheel.reactive.Signal;

public class Communicator {

	static public void start(User user, OldNetwork network, BusinessSource businessSource) {
		new Communicator(user, network, businessSource);
	}

	private Communicator(User user, OldNetwork network, BusinessSource businessSource) {
		Business business = businessSource.output();
		
		ServerStarter.start(user, network, business.sneerPort());
		Spider.start(network, business.contacts(), businessSource.contactOnlineSetter());
	}
	
}
