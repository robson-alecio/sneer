package sneer.kernel.communication;

import java.io.IOException;

import wheel.io.Log;
import wheel.io.network.OldNetwork;
import wheel.io.ui.User;
import wheel.lang.Threads;
import wheel.lang.exceptions.FriendlyException;
import wheel.reactive.Receiver;
import wheel.reactive.Signal;

class ServerStarter implements Receiver<Integer> {
	
	private final User _user;
	private final OldNetwork _network;
	private volatile Server _server;


	ServerStarter(User user, OldNetwork network, Signal<Integer> sneerPort) {
		_user = user;
		_network = network;

		sneerPort.addTransientReceiver(this);
		Threads.preventFromBeingGarbageCollected(this);
	}

	@Override
	public void receive(Integer newPort) {
		if (_server != null) _server.stop();
		
		try {
			_server = Server.start(_network, newPort);
		} catch (IOException e) {
			Log.log(e);
		} catch (FriendlyException e) {
			_user.acknowledgeFriendlyException(e);
		}
	}

}