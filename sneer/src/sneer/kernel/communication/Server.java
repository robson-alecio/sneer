package sneer.kernel.communication;

import java.io.IOException;
import java.net.BindException;

import sneer.kernel.business.Business;
import wheel.io.network.ObjectServerSocket;
import wheel.io.network.OldNetwork;
import wheel.io.ui.User;
import wheel.reactive.Receiver;

class Server {

	static void start(User user, OldNetwork network, Business business) {
		new Server(user, network, business);
	}

	private Server(User user, OldNetwork network, Business business) {
		_user = user;
		_network = network;
		business.sneerPort().addTransientReceiver(mySneerPortReceiver());
	}

	private ObjectServerSocket _server;
	private final User _user;
	private final OldNetwork _network;
	
	private Receiver<Integer> mySneerPortReceiver() {
		return new Receiver<Integer>(){
			public void receive(Integer newPort) {
				stopServer();
				startServer(newPort);
			}
		};
	}
	
	private void stopServer() {
		if (_server == null) return;
		try {
			_server.close();
		} catch (IOException e) {
			_user.acknowledge(e);
		}
		_server = null;
	}

	private void startServer(int port) {
		try {
			_server = _network.openObjectServerSocket(port);
		} catch (BindException ignored) {
			_user.acknowledgeUnexpectedProblem("Unable to listen on port " + port + ".", help(port));
		} catch (IOException e) {
			_user.acknowledge(e);
		}
	}

	private String help(int port) {
		return
		" The port you chose ("+ port + ") might be blocked or in use by\n" +
		" another application, including another Sneer instance.\n" +
		"\n" +
		" You can have two instances of Sneer running if you like,\n" +
		" for two people, for example, but each one has to use a\n" +
		" different port. If there is another application using\n" +
		" that same port, you either have to close it, configure\n" +
		" it to use a different port, or configure Sneer to use a\n" +
		" different port.";
	}

}
