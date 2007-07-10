package sneer.kernel.communication.impl;

import java.io.IOException;
import java.net.BindException;

import wheel.io.Log;
import wheel.io.network.ObjectServerSocket;
import wheel.io.network.ObjectSocket;
import wheel.io.network.OldNetwork;
import wheel.io.ui.User;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.lang.exceptions.FriendlyException;
import wheel.reactive.Signal;
import static wheel.i18n.Language.*;

class SocketAccepter implements Omnivore<Integer> {
	
	private final User _user;
	private final OldNetwork _network;
	private final Omnivore<ObjectSocket> _socketOmnivore;
	private volatile BoundSocketAccepter _current;


	SocketAccepter(User user, OldNetwork network, Signal<Integer> sneerPort, Omnivore<ObjectSocket> socketOmnivore) {
		_user = user;
		_network = network;
		_socketOmnivore = socketOmnivore;

		sneerPort.addTransientReceiver(this);
		Threads.preventFromBeingGarbageCollected(this);
	}

	@Override
	public void consume(Integer newPort) {
		if (_current != null) _current.stop();
		
		try {
			_current = new BoundSocketAccepter(newPort);
		} catch (IOException e) {
			Log.log(e);
		} catch (FriendlyException e) {
			_user.acknowledgeFriendlyException(e);
		}
	}

	
	private String help(int port) {
		return translate(
		"The port you chose (%1$s) might be blocked or in use by\n" +
		"another application, including another Sneer instance.\n" +
		"\n" +
		"You can have two instances of Sneer running if you like,\n" +
		"for two people, for example, but each one has to use a\n" +
		"different port. If there is another application using\n" +
		"that same port, you either have to close it, configure\n" +
		"it to use a different port, or configure Sneer to use a\n" +
		"different port.",port);
	}
	
	
	class BoundSocketAccepter {

		private BoundSocketAccepter(int port) throws IOException, FriendlyException {
			try {
				_serverSocket = _network.openObjectServerSocket(port);
			} catch (BindException e) {
				throw new FriendlyException(translate("Unable to listen on port %1$s.",port), help(port));
			}
			
			startAccepting();
		}

		private final ObjectServerSocket _serverSocket;
		private volatile boolean _isStopped = false;
		
		
		private void startAccepting() {
			Threads.startDaemon(new Runnable() { @Override public void run() {
				while (!_isStopped) accept();
			} } );
		}

		private void accept() {
			try {
				_socketOmnivore.consume(_serverSocket.accept());
			} catch (IOException e) {}
		}
		

		void stop() {
			try {
				_isStopped = true;
				_serverSocket.close();
			} catch (IOException ignored) {}
		}

	}
}