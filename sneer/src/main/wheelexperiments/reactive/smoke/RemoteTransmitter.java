package wheelexperiments.reactive.smoke;

import java.io.IOException;

import wheelexperiments.environment.network.ObjectSocket;
import wheelexperiments.environment.network.OldNetwork;
import wheelexperiments.reactive.Receiver;

public class RemoteTransmitter<T> implements Receiver<T> {

	private ObjectSocket _socket;

	public RemoteTransmitter(OldNetwork network) throws IOException {
		_socket = network.openSocket("localhost", 7893);
	}

	public void receive(T newValue) {
		try {
			_socket.writeObject(newValue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
