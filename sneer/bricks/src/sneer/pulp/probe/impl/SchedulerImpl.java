/**
 * 
 */
package sneer.pulp.probe.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import sneer.kernel.container.Inject;
import sneer.pulp.connection.ByteConnection.Packet;
import sneer.pulp.connection.ByteConnection.PacketScheduler;
import sneer.pulp.contacts.Contact;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.Tuple;
import sneer.pulp.tuples.TupleSpace;
import wheel.io.serialization.Serializer;
import wheel.io.serialization.impl.XStreamBinarySerializer;
import wheel.lang.Omnivore;
import wheel.lang.Threads;

final class SchedulerImpl implements PacketScheduler, Omnivore<Tuple> {

	@Inject static private TupleSpace _tuples;
	@Inject static private KeyManager _keyManager;
	
	static private final Serializer _serializer = new XStreamBinarySerializer();

	
	private final List<byte[]> _toSend = Collections.synchronizedList(new LinkedList<byte[]>());
	private final Contact _contact;
	private PublicKey _contactsPK;
	
	SchedulerImpl(Contact contact) {
		_contact = contact;
		_tuples.addSubscription(Tuple.class, this);
	}
	
	@Override
	public Packet highestPriorityPacketToSend() {
		while (_toSend.isEmpty())
			Threads.sleepWithoutInterruptions(10);

		synchronized (_toSend) {
			int newest = _toSend.size() - 1;
			return new MyPacket(_toSend.get(newest), newest);
		}
	}
	
	@Override
	public void packetWasSent(Packet packet) {
		_toSend.remove(((MyPacket)packet)._position);
	}


	@Override
	public void consume(Tuple tuple) {
		if (!isClearToSend(tuple)) return;
		
		_toSend.add(_serializer.serialize(tuple));
	}

	private boolean isClearToSend(Tuple tuple) {
		initContactsPKIfNecessary();
		if (_contactsPK == null) return false;
		
		return !isEcho(tuple);
	}

	private boolean isEcho(Tuple tuple) {
		return _contactsPK.equals(tuple.publisher());
	}

	private void initContactsPKIfNecessary() {
		if (_contactsPK != null) return;
		_contactsPK = _keyManager.keyGiven(_contact);
	}

}

class MyPacket implements Packet {

	final int _position;
	private final byte[] _payload;

	MyPacket(byte[] payload, int position) {
		_payload = payload;
		_position = position;
	}

	@Override
	public byte[] payload() {
		return _payload;
	}

}
