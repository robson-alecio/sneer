package sneer.kernel.communication;


import sneer.kernel.business.Business;
import sneer.kernel.business.BusinessSource;
import wheel.io.network.OldNetwork;
import wheel.io.ui.User;
import wheel.lang.exceptions.NotImplementedYet;

public class Communicator {

	public Communicator(User user, OldNetwork network, BusinessSource businessSource) {
		Business business = businessSource.output();
		
		new ServerStarter(user, network, business.sneerPort());
		Spider.start(network, business.contacts(), businessSource.contactOnlineSetter());
	}

	public Operator operatorFor(String fullAppName) {
		throw new NotImplementedYet();
	}
	
}
