package sneer.bricks.pulp.probe.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.LinkedList;
import java.util.List;

import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.network.computers.sockets.connections.ByteConnection.PacketScheduler;
import sneer.bricks.pulp.serialization.Serializer;
import sneer.foundation.brickness.Tuple;

class SchedulerImpl implements PacketScheduler {

	private final Serializer _serializer = my(Serializer.class);

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
			my(Threads.class).waitWithoutInterruptions(this);

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