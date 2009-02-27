/**
 * 
 */
package sneer.pulp.probe.impl;

import sneer.kernel.container.PublicKey;
import sneer.kernel.container.Tuple;
import sneer.pulp.contacts.Contact;
import sneer.pulp.distribution.filtering.TupleFilterManager;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.tuples.TupleSpace;
import wheel.lang.Consumer;
import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;
import static sneer.brickness.Environments.my;

final class ProbeImpl implements Consumer<Tuple> {

	private final TupleSpace _tuples = my(TupleSpace.class);
	private final KeyManager _keyManager = my(KeyManager.class);
	private final TupleFilterManager _filter = my(TupleFilterManager.class);

	
	private final Contact _contact;
	private PublicKey _contactsPK;
	
	private final Object _isOnlineMonitor = new Object();
	private boolean _isOnline = false;
	final SchedulerImpl _scheduler = new SchedulerImpl();
	
	@SuppressWarnings("unused")
	private final Receiver<Boolean> _receiverToAvoidGC;

	
	ProbeImpl(Contact contact, Signal<Boolean> isOnline) {
		_contact = contact;
		_receiverToAvoidGC = createIsOnlineReceiver(isOnline);
	}


	private Receiver<Boolean> createIsOnlineReceiver(Signal<Boolean> isOnlineSignal) {
		return new Receiver<Boolean>(isOnlineSignal){ @Override public void consume(Boolean isOnline) {
			dealWithIsOnline(isOnline);
		}};
	}

	private void dealWithIsOnline(boolean isOnline) {
		synchronized (_isOnlineMonitor) {
			boolean wasOnline = _isOnline;
			_isOnline = isOnline;

			if (isOnline) {
				_tuples.addSubscription(Tuple.class, this);
			} else if (wasOnline) {
				_tuples.removeSubscriptionAsync(this);
				_scheduler.drain();
			}
		}
	}


	@Override
	public void consume(Tuple tuple) {
		if (!isClearToSend(tuple)) return;
		
		synchronized (_isOnlineMonitor) {
			if (!_isOnline)	return;
			_scheduler.add(tuple);
		}
	}

	private boolean isClearToSend(Tuple tuple) {
		initContactsPKIfNecessary();
		if (_contactsPK == null)
			return false;

		if (_filter.isBlocked(tuple)) return false;
		
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