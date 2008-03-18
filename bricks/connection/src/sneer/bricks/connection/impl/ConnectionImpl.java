package sneer.bricks.connection.impl;

import sneer.bricks.connection.Connection;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;

public class ConnectionImpl implements Connection {

	private Source<Boolean> source;
	
	public ConnectionImpl() {
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
