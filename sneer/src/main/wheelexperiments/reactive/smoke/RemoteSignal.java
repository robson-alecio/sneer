package wheelexperiments.reactive.smoke;

import java.io.IOException;

import wheel.experiments.Cool;
import wheel.experiments.environment.network.ObjectServerSocket;
import wheel.experiments.environment.network.ObjectSocket;
import wheel.experiments.environment.network.OldNetwork;
import wheelexperiments.reactive.Notifier;

public class RemoteSignal<T> extends Notifier<T> {

	private ObjectSocket _socket;
	private T _currentValue;

	public RemoteSignal(OldNetwork network) throws IOException {
		final ObjectServerSocket server = network.openObjectServerSocket(7893);
		Cool.startDaemon(new Runnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				try {
					_socket = server.accept();
					while (true)
						_currentValue = (T)_socket.readObject();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	public T currentValue() {
		return _currentValue;
	}

}
