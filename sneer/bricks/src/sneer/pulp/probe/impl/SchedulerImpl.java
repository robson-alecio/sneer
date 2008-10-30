/**
 * 
 */
package sneer.pulp.probe.impl;

import java.util.LinkedList;
import java.util.List;

import sneer.kernel.container.Inject;
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

	
	private final Contact _contact;
	private PublicKey _contactsPK;

	private final List<Tuple> _toSend = new LinkedList<Tuple>();
	private int _lastTupleSent;

	
	SchedulerImpl(Contact contact) {
		_contact = contact;
		_tuples.addSubscription(Tuple.class, this);
	}
	
	@Override
	public byte[] highestPriorityPacketToSend() {
		synchronized (_toSend) {
			if (_toSend.isEmpty())
				Threads.waitWithoutInterruptions(_toSend);
		}

		return _serializer.serialize(mostRecentTuple()); //Optimize: Use same serialized form of this tuple for all interested contacts.
	}

	private Tuple mostRecentTuple() {
		synchronized (_toSend) {
			_lastTupleSent = _toSend.size() - 1;
			return _toSend.get(_lastTupleSent);
		}
	}

	@Override
	public void previousPacketWasSent() {
		synchronized (_toSend) {
			_toSend.remove(_lastTupleSent);
		}
	}


	@Override
	public void consume(Tuple tuple) {
		if (!isClearToSend(tuple)) return;
		
		synchronized (_toSend) {
			_toSend.add(tuple); //Fix This is a leak: dont keep packets for contacts that are offline.
			_toSend.notify();
		}
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


