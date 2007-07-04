package sneer.kernel.communication.impl;

import wheel.io.Connection;
import wheel.lang.Omnivore;
import wheel.reactive.Receiver;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.SourceImpl;

class MuxedConnection implements Connection {

	MuxedConnection(String connectionId, Connection sharedConnection) {
		_id = connectionId;
		_sharedConnection = sharedConnection;
		_sharedConnection.input().addReceiver(myReceiver());
	}

	private final String _id;
	private final Connection _sharedConnection;
	private final Omnivore<Object> _myOutput = createMyOutput();
	private Source<Object> _myInput = new SourceImpl<Object>(null);

	private Receiver<Object> myReceiver() {
		return new Receiver<Object>() { public void receive(Object packetObject) {
			MuxedPacket packet = (MuxedPacket)packetObject; //Refactor: Eliminate this cast using generics.
			if (packet._muxedConnectionId == _id)
				_myInput.setter().consume(packet._contents);
		} };
	}

	private Omnivore<Object> createMyOutput() {
		return new Omnivore<Object>() {	public void consume(Object object) {
			_sharedConnection.output().consume(new MuxedPacket(_id, object));
		} };
	}

	public Signal<Object> input() {
		return _myInput.output();
	}

	public Omnivore<Object> output() {
		return _myOutput;
	}

}
