/**
 * 
 */
package sneer.bricks.pulp.probe.impl;

import static sneer.foundation.commons.environments.Environments.my;
import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.contacts.Contact;
import sneer.bricks.pulp.distribution.filtering.TupleFilterManager;
import sneer.bricks.pulp.keymanager.KeyManager;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.brickness.PublicKey;
import sneer.foundation.brickness.Tuple;

final class ProbeImpl implements Consumer<Tuple> {

	private final TupleSpace _tuples = my(TupleSpace.class);
	private final KeyManager _keyManager = my(KeyManager.class);
	private final TupleFilterManager _filter = my(TupleFilterManager.class);

	
	private final Contact _contact;
	private PublicKey _contactsPK;
	
	private final Object _isOnlineMonitor = new Object();
	private boolean _isOnline = false;
	final SchedulerImpl _scheduler = new SchedulerImpl();

	@SuppressWarnings("unused") private final Object _referenceToAvoidGc;

	ProbeImpl(Contact contact, Signal<Boolean> isOnlineSignal) {
		_contact = contact;
		_referenceToAvoidGc = my(Signals.class).receive(isOnlineSignal, new Consumer<Boolean>(){ @Override public void consume(Boolean isOnline) {
			dealWithIsOnline(isOnline);
		}});
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