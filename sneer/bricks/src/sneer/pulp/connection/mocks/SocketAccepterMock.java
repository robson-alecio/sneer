package sneer.pulp.connection.mocks;

import sneer.pulp.connection.SocketAccepter;
import sneer.pulp.events.EventNotifier;
import sneer.pulp.events.EventSource;
import sneer.pulp.network.ByteArraySocket;
import wheel.reactive.impl.EventNotifierImpl;

public class SocketAccepterMock implements SocketAccepter {

	public final EventNotifier<ByteArraySocket> _notifier = new EventNotifierImpl<ByteArraySocket>();

	@Override
	public EventSource<ByteArraySocket> lastAcceptedSocket() {
		return _notifier.output();
	}

}
