package sneer.kernel.communication;

import java.io.IOException;
import java.net.BindException;

import org.prevayler.Prevayler;

import sneer.kernel.business.BusinessSource;
import wheel.io.network.OldNetwork;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.reactive.Receiver;

public class Communicator {

	private final BusinessSource _business;
	private ParallelServer _server;
	private final User _user;
	private final OldNetwork _network;

	public Communicator(User user, OldNetwork network, Prevayler prevayler) {
		_user = user;
		_network = network;
		_business = (BusinessSource)prevayler.prevalentSystem();
		_business.output().sneerPort().addReceiver(new Receiver<Integer>(){
			public void receive(Integer newPort) {
				stopServer();
				startServer(newPort);
			}
		});
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
			ParallelServer.User user = new ParallelServer.User(){
				public boolean authorizeConnectionFrom(String name) {
					try {
						return _user.confirm("Somebody claiming to be '" + name + "' is trying to connect here. Do you want to accept this connection? Were you expecting this connection right now?");
					} catch (CancelledByUser ignored) {
						return false;
					}
				}
			};
			_server = new ParallelServer(_business, _network.openObjectServerSocket(port), user, _user.catcher());
		} catch (BindException ignored) {
			String help =
				" The port you chose ("+ port + ") might be blocked or in use by\n" +
				" another application, including another Sneer instance.\n" +
				"\n" +
				" You can have two instances of Sneer running if you like,\n" +
				" for two people for example, but each one has to use a\n" +
				" different port. If there is another application using\n" +
				" that same port, you have either to close it, configure\n" +
				" it to use a different port, or configure Sneer to use a\n" +
				" different port.";
			_user.acknowledgeUnexpectedProblem("Unable to listen on port " + port + ".", help);
		} catch (IOException e) {
			_user.acknowledge(e);
		}
	}

}
