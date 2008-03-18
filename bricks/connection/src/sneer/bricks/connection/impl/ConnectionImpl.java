package sneer.bricks.connection.impl;

import sneer.bricks.connection.Connection;
import wheel.io.network.ObjectSocket;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;

public class ConnectionImpl implements Connection {

	private Source<Boolean> source;
	
	private ObjectSocket _socket;
	
	public ConnectionImpl(ObjectSocket socket) {
		_socket = socket;
		source = new SourceImpl<Boolean>(true);
	}
	
	@Override
	public Signal<Boolean> isOnline() {
		return source.output();
	}

	@Override
	public void close() {
		source.setter().consume(Boolean.FALSE);
	}
}
