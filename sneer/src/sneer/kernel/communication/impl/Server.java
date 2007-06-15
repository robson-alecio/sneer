package sneer.kernel.communication.impl;

import java.io.IOException;
import java.net.BindException;

import wheel.io.Log;
import wheel.io.network.ObjectServerSocket;
import wheel.io.network.ObjectSocket;
import wheel.io.network.OldNetwork;
import wheel.lang.Threads;
import wheel.lang.exceptions.FriendlyException;

class Server {

	static Server start(OldNetwork network, int port) throws IOException, FriendlyException {
		return new Server(network, port);
	}

	private Server(OldNetwork network, int port) throws IOException, FriendlyException {
		try {
			_serverSocket = network.openObjectServerSocket(port);
		} catch (BindException e) {
			throw new FriendlyException("Unable to listen on port " + port + ".", help(port));
		}
		
		startAccepting();
	}

	private final ObjectServerSocket _serverSocket;
	private volatile boolean _isStopped = false;
	
	
	private void startAccepting() {
		Threads.startDaemon(new Runnable(){
			@Override
			public void run() {
				while (!_isStopped) accept();
			}
		});
		
	}

	private void accept() {
		try {
			serve(_serverSocket.accept());
		} catch (IOException e) {
			if (_isStopped) return;
			Log.log(e);
		}
	}
	
	private void serve(final ObjectSocket socket) {
		Threads.startDaemon(new Runnable() {

			@Override
			public void run() {
				while (true){
					try {
						Object readObject = socket.readObject();
						System.out.println("Received: " + readObject);
					} catch (IOException e) {
						// Implement Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// Implement Auto-generated catch block
						e.printStackTrace();
					} 
				}
			}
		});
		
	}

	void stop() {
		try {
			_isStopped = true;
			_serverSocket.close();
		} catch (IOException ignored) {}
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
