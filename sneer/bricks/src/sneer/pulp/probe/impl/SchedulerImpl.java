package sneer.pulp.probe.impl;

import java.util.LinkedList;
import java.util.List;

import sneer.pulp.connection.ByteConnection.PacketScheduler;
import sneer.pulp.tuples.Tuple;
import wheel.io.serialization.Serializer;
import wheel.io.serialization.impl.XStreamBinarySerializer;
import wheel.lang.Threads;

class SchedulerImpl implements PacketScheduler {

	static private final Serializer _serializer = new XStreamBinarySerializer();

	private final List<Tuple> _toSend = new LinkedList<Tuple>();
	private boolean _wasDrained = false;
	private int _lastTupleSent;

	
	@Override
	public byte[] highestPriorityPacketToSend() {
		Tuple tuple = highestPriorityTupleToSend();
		return _serializer.serialize(tuple); //Optimize: Use same serialized form of this tuple for all interested contacts.
	}

	synchronized private Tuple highestPriorityTupleToSend() {
		if (_toSend.isEmpty())
			Threads.waitWithoutInterruptions(this);

		_lastTupleSent = _toSend.size() - 1;
		return _toSend.get(_lastTupleSent);
	}


	@Override
	public synchronized void previousPacketWasSent() {
		if (_wasDrained) return;
		_toSend.remove(_lastTupleSent);
	}

	synchronized void drain() {
		_wasDrained = true;
		_toSend.clear();
	}

	synchronized void add(Tuple tuple) {
		_wasDrained = false;
		_toSend.add(tuple);
		notify();
	}

}
