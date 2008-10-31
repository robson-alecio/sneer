package sneer.pulp.connection.mocks;

import sneer.pulp.connection.SocketAccepter;
import sneer.pulp.network.ByteArraySocket;
import wheel.reactive.EventNotifier;
import wheel.reactive.EventSource;
import wheel.reactive.impl.EventNotifierImpl;

public class SocketAccepterMock implements SocketAccepter {

	public final EventNotifier<ByteArraySocket> _notifier = new EventNotifierImpl<ByteArraySocket>();

	@Override
	public EventSource<ByteArraySocket> lastAcceptedSocket() {
		return _notifier.output();
	}

}
