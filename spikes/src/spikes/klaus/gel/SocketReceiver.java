package spikes.klaus.gel;

import java.io.IOException;

import spikes.wheel.io.network.ObjectServerSocket;
import spikes.wheel.io.network.OldNetwork;


public class SocketReceiver {

	public SocketReceiver(OldNetwork network) {
		_network = network;
	}

	
	private final OldNetwork _network;
	private ObjectServerSocket _serverSocket;
	
	
	public void change(ServerPortChoice choice) {
		if (_serverSocket != null) _serverSocket.close();
		openServerSocket(choice);
	}


	private void openServerSocket(ServerPortChoice choice) {
		try {
			_serverSocket = _network.openObjectServerSocket(choice.port());
		} catch (IOException e) {
			//_context.process(new SocketReceiverState);
			// "Unable to listen on port X ...Trying every 10 seconds."
		}
	}
	
}
