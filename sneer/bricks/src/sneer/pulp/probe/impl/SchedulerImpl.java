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
import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;

final class SchedulerImpl implements PacketScheduler, Omnivore<Tuple> {

	@Inject static private TupleSpace _tuples;
	@Inject static private KeyManager _keyManager;
	
	static private final Serializer _serializer = new XStreamBinarySerializer();

	
	private final Contact _contact;
	private PublicKey _contactsPK;

	private boolean _isOnline = false;

	private final List<Tuple> _toSend = new LinkedList<Tuple>();
	private boolean _toSendWasDrained = false;
	private int _lastTupleSent;

	
	SchedulerImpl(Contact contact, Signal<Boolean> isOnline) {
		_contact = contact;
		
		new Receiver<Boolean>(isOnline){ @Override public void consume(Boolean cameOnline) {
			if (cameOnline)
				_tuples.addSubscription(Tuple.class, SchedulerImpl.this);
			else if (_isOnline) {
				_tuples.removeSubscription(SchedulerImpl.this);
			}

			synchronized (_toSend) {
				_isOnline = cameOnline;
				if (!_isOnline)
					drainToSendQueue();
			}
		}};

	}

	private void drainToSendQueue() {
		_toSend.clear();
		_toSendWasDrained = true;
	}
	

	@Override
	public byte[] highestPriorityPacketToSend() {
		Tuple result;
		synchronized (_toSend) {
			if (_toSend.isEmpty())
				Threads.waitWithoutInterruptions(_toSend);
			result = mostRecentTuple();
		}
		return _serializer.serialize(result); //Optimize: Use same serialized form of this tuple for all interested contacts.
	}

	private Tuple mostRecentTuple() {
		_lastTupleSent = _toSend.size() - 1;
		return _toSend.get(_lastTupleSent);
	}

	@Override
	public void previousPacketWasSent() {
		synchronized (_toSend) {
			if (_toSendWasDrained) return;
			_toSend.remove(_lastTupleSent);
		}
	}


	@Override
	public void consume(Tuple tuple) {
		synchronized (_toSend) {
			if (!_isOnline) return;
		}

		if (!isClearToSend(tuple)) return;
		
		synchronized (_toSend) {
			if (!_isOnline) return;
			_toSendWasDrained = false;
			_toSend.add(tuple);
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


