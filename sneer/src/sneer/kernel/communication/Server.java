package sneer.kernel.communication;

import java.io.IOException;
import java.net.BindException;

import wheel.io.Log;
import wheel.io.network.ObjectServerSocket;
import wheel.io.network.OldNetwork;
import wheel.io.ui.User;
import wheel.lang.Threads;

class Server {

	static Server start(User user, OldNetwork network, int port) {
		return new Server(user, network, port);
	}

	private Server(User user, OldNetwork network, int port) {
		try {
			_serverSocket = network.openObjectServerSocket(port);
			startAccepting();
		} catch (BindException ignored) {
			user.acknowledgeUnexpectedProblem("Unable to listen on port " + port + ".", help(port)); //Refactor: Make user have consumers of messages.
		} catch (IOException e) {
			user.acknowledge(e);
		}
	}

	private ObjectServerSocket _serverSocket;
	
	private void startAccepting() {
		Threads.startDaemon(new Runnable(){
			@Override
			public void run() {
				while (true) accept();
			}
		});
	}

	private void accept() {
		try {
			_serverSocket.accept();
		} catch (IOException e) {
			Log.log(e);
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

	void stop() {
		try {
			_serverSocket.close();
		} catch (IOException ignored) {}
	}

}
