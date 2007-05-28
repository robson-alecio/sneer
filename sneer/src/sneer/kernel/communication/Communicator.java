package sneer.kernel.communication;

import sneer.kernel.business.Business;
import sneer.kernel.business.BusinessSource;
import wheel.io.network.OldNetwork;
import wheel.io.ui.User;
import wheel.lang.Threads;
import wheel.reactive.Receiver;
import wheel.reactive.Signal;

public class Communicator {

	static public void start(User user, OldNetwork network, BusinessSource businessSource) {
		new Communicator(user, network, businessSource);
	}

	private Communicator(User user, OldNetwork network, BusinessSource businessSource) {
		_user = user;
		_network = network;
		Business business = businessSource.output();
		
		startServing(business.sneerPort());
		Spider.start(network, business.contacts());
	}


	private final User _user;
	private final OldNetwork _network;
	private Server _server;

	private class ServerStarter implements Receiver<Integer> {
		
		@Override
		public void receive(Integer newPort) {
			if (_server != null) _server.stop();
			_server = Server.start(_user, _network, newPort);
		}
		
	}

	private void startServing(Signal<Integer> sneerPort) {
		sneerPort.addTransientReceiver(new ServerStarter());
	}
	
}
