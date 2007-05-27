package sneer.kernel.communication;

import java.io.IOException;
import java.net.BindException;

import sneer.kernel.business.Business;
import wheel.io.network.ObjectServerSocket;
import wheel.io.network.OldNetwork;
import wheel.io.ui.User;
import wheel.reactive.Receiver;

public class Communicator {

	static public void start(User user, OldNetwork network, Business business) {
		Server.start(user, network, business);
		Spider.start(network, business);
	}

}
