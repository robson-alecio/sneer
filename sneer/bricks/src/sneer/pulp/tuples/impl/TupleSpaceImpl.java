package sneer.pulp.tuples.impl;

import static wheel.lang.Types.cast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import sneer.kernel.container.Inject;
import sneer.pulp.clock.Clock;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.tuples.Tuple;
import sneer.pulp.tuples.TupleSpace;
import wheel.lang.Consumer;
import wheel.lang.Types;

public class TupleSpaceImpl implements TupleSpace {

	@Inject static private KeyManager _keyManager;
	@Inject static private Clock _clock;
	//@Inject static private PersistenceConfig _config;
	
	//Refactor The synchronization will no longer be necessary when the container guarantees synchronization of model bricks.
	static class Subscription {

		private final Consumer<? super Tuple> _subscriber;
		private final Class<? extends Tuple> _tupleType;

		<T extends Tuple> Subscription(Consumer<? super T> subscriber, Class<T> tupleType) {
			_subscriber = cast(subscriber);
			_tupleType = tupleType;
		}

		void filterAndNotify(Tuple tuple) {
			if (!Types.instanceOf(tuple, _tupleType))
				return;
			
			_subscriber.consume(tuple);
		}


	}

	private static final int TRANSIENT_TUPLES_MAX = 1000;
	private final Set<Tuple> _transientTuples = new LinkedHashSet<Tuple>();
	private final List<Subscription> _subscriptions = new ArrayList<Subscription>();
	
	private final Set<Class<? extends Tuple>> _typesToKeep = new HashSet<Class<? extends Tuple>>();
	private final Set<Tuple> _keptTuples = new HashSet<Tuple>();
	
	//private final Prevayler _prevayler;

	
	TupleSpaceImpl() {
		//_prevayler = PrevaylerFactory.createPrevayler(_keptTuples, _config.persistenceDirectory().getAbsolutePath());
	}

	
	@Override
	public synchronized void publish(Tuple tuple) {
		stamp(tuple);
		acquire(tuple);
	}

	@Override
	public synchronized void acquire(Tuple tuple) {
		if (!_transientTuples.add(tuple)) return;
		capTransientTuples();
		keepIfNecessary(tuple);
				
		for (Subscription subscription : _subscriptions)
			subscription.filterAndNotify(tuple);
	}

	
	private void keepIfNecessary(Tuple tuple) {
		for (Class<? extends Tuple> typeToKeep : _typesToKeep)
			if (Types.instanceOf(tuple, typeToKeep)) {
				_keptTuples.add(tuple);
				return;
			}
	}

	
	private void stamp(Tuple tuple) {
		tuple.stamp(_keyManager.ownPublicKey(), _clock.time());
	}

	private void capTransientTuples() {
		if (_transientTuples.size() <= TRANSIENT_TUPLES_MAX) return;

		Iterator<Tuple> tuplesIterator = _transientTuples.iterator();
		tuplesIterator.next();
		tuplesIterator.remove();
		
	}

	@Override
	public synchronized <T extends Tuple> void addSubscription(Class<T> tupleType,	Consumer<? super T> subscriber) {
		_subscriptions.add(new Subscription(subscriber, tupleType));
	}
	
	@Override
	public synchronized <T extends Tuple> void removeSubscription(Object subscriber) {
		for (Subscription victim : _subscriptions)
			if (victim._subscriber == subscriber) {
				_subscriptions.remove(victim);
				return;
			} 

		throw new IllegalArgumentException("Subscription not found.");
	}

	@Override
	public synchronized void keep(Class<? extends Tuple> tupleType) {
		_typesToKeep.add(tupleType);
	}

	@Override
	public synchronized Collection<Tuple> keptTuples() {
		return new ArrayList<Tuple>(_keptTuples);
	}

	@Override
	public void crash() {
		//_prevayler.close();
	}

}
