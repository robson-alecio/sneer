package sneer.pulp.connection.mocks;

import sneer.pulp.connection.SocketAccepter;
import sneer.pulp.events.EventNotifier;
import sneer.pulp.events.EventNotifierFactory;
import sneer.pulp.events.EventSource;
import sneer.pulp.network.ByteArraySocket;
import static sneer.commons.environments.Environments.my;



public class SocketAccepterMock implements SocketAccepter {

	public final EventNotifier<ByteArraySocket> _notifier = my(EventNotifierFactory.class).create();

	@Override
	public EventSource<ByteArraySocket> lastAcceptedSocket() {
		return _notifier.output();
	}

}
