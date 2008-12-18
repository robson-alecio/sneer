package sneer.pulp.tuples.impl;

import static wheel.lang.Environments.my;
import static wheel.lang.Types.cast;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.foundation.serialization.XStreamSerializer;

import snapps.wind.impl.bubble.Bubble;
import sneer.pulp.clock.Clock;
import sneer.pulp.config.persistence.PersistenceConfig;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.threadpool.Stepper;
import sneer.pulp.threadpool.ThreadPool;
import sneer.pulp.tuples.Tuple;
import sneer.pulp.tuples.TupleSpace;
import sneer.pulp.tuples.config.TupleSpaceConfig;
import wheel.lang.Consumer;
import wheel.lang.Environments;
import wheel.lang.Environments.Memento;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.impl.ListRegisterImpl;

public class TupleSpaceImpl implements TupleSpace {

	//Refactor The synchronization will no longer be necessary when the container guarantees synchronization of model bricks.
	static class Subscription {

		private final Consumer<? super Tuple> _subscriber;
		private final Class<? extends Tuple> _tupleType;
		private final Memento _environment;

		<T extends Tuple> Subscription(Consumer<? super T> subscriber, Class<T> tupleType) {
			_subscriber = cast(subscriber);
			_tupleType = tupleType;
			_environment = Environments.memento();
		}

		void filterAndNotify(final Tuple tuple) {
			if (!_tupleType.isInstance(tuple))
				return;
			
			my(ThreadPool.class).dispatch(_environment, new Runnable() { @Override public void run() {
				_subscriber.consume(tuple);
			}});
		}
	}

	private static final int TRANSIENT_CACHE_SIZE = 1000;
	private static final Subscription[] SUBSCRIPTION_ARRAY = new Subscription[0];

	private final KeyManager _keyManager = my(KeyManager.class);
	private final Clock _clock = my(Clock.class);
	private final PersistenceConfig _persistenceConfig = my(PersistenceConfig.class);
	private final ThreadPool _threads = my(ThreadPool.class);
	private final TupleSpaceConfig _config = my(TupleSpaceConfig.class);

	private final List<Subscription> _subscriptions = Collections.synchronizedList(new ArrayList<Subscription>());

	private final Set<Tuple> _transientTupleCache = new LinkedHashSet<Tuple>();
	private final Set<Class<? extends Tuple>> _typesToKeep = new HashSet<Class<? extends Tuple>>();
	private final ListRegister<Tuple> _keptTuples;

	private final BlockingQueue<Tuple> _acquisitionQueue;

	private final Object _publicationMonitor = new Object();


	
	TupleSpaceImpl() {
		_keptTuples = Bubble.wrapStateMachine(prevayler(new ListRegisterImpl<Tuple>()));
		_acquisitionQueue = _config.isAcquisitionSynchronous() ? null : new LinkedBlockingQueue<Tuple>(); 
		if (_acquisitionQueue != null) {
			_threads.registerStepper(createStepper());
		}
	}


	private Stepper createStepper() {
		return new Stepper() { @Override public boolean step() {
			Tuple tuple = popAcquisition();
			if (tuple != null) processAcquisition(tuple);
			return true;
		}};
	}
		
	private Tuple popAcquisition() {
		try {
			return _acquisitionQueue.take();
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}


	private Prevayler prevayler(Serializable system) {
		PrevaylerFactory factory = prevaylerFactory(system);

		try {
			return factory.create();
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (ClassNotFoundException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}


	private PrevaylerFactory prevaylerFactory(Serializable system) {
		PrevaylerFactory factory = new PrevaylerFactory();
		factory.configurePrevalentSystem(system);
		factory.configurePrevalenceDirectory(directory());
		factory.configureJournalSerializer(new XStreamSerializer());
		factory.configureTransactionFiltering(false);
		return factory;
	}


	private String directory() {
		return new File(_persistenceConfig.persistenceDirectory(), "tuplespace").getAbsolutePath();
	}

	
	@Override
	public void publish(Tuple tuple) {
		synchronized (_publicationMonitor ) {
			stamp(tuple);
			acquire(tuple);
		}
	}

	@Override
	public void acquire(Tuple tuple) {
		if (_acquisitionQueue != null) {
			enqueue(tuple);
			return;
		}
		processAcquisition(tuple);
	}


	private void enqueue(Tuple tuple) {
		try {
			_acquisitionQueue.put(tuple);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}


	private synchronized void processAcquisition(Tuple tuple) {
		if (!_transientTupleCache.add(tuple)) return;
		capTransientTuples();
		
		if (isAlreadyKept(tuple)) return;
		keepIfNecessary(tuple);
				
		notifySubscriptions(tuple);
	}


	private void notifySubscriptions(Tuple tuple) {
		for (Subscription subscription : _subscriptions.toArray(SUBSCRIPTION_ARRAY))
			subscription.filterAndNotify(tuple);
	}


	private void keepIfNecessary(Tuple tuple) {
		if (shouldKeep(tuple)) keep(tuple);
	}

	
	private boolean shouldKeep(Tuple tuple) {
		for (Class<? extends Tuple> typeToKeep : _typesToKeep) //Optimize
			if (typeToKeep.isInstance(tuple))
				return true;

		return false;
	}


	private boolean isAlreadyKept(Tuple tuple) {
		return _keptTuples.output().currentIndexOf(tuple) != -1;  //Optimize
	}


	private void keep(Tuple tuple) {
		_keptTuples.adder().consume(tuple);
	}

	
	private void stamp(Tuple tuple) {
		tuple.stamp(_keyManager.ownPublicKey(), _clock.time());
	}

	private void capTransientTuples() {
		if (_transientTupleCache.size() <= TRANSIENT_CACHE_SIZE) return;

		Iterator<Tuple> tuplesIterator = _transientTupleCache.iterator();
		tuplesIterator.next();
		tuplesIterator.remove();
		
	}

	@Override
	public <T extends Tuple> void addSubscription(Class<T> tupleType,	Consumer<? super T> subscriber) {
		Subscription subscription = new Subscription(subscriber, tupleType);

		for (Tuple kept : keptTuples())
			subscription.filterAndNotify(kept);

		_subscriptions.add(subscription);
	}
	
	/** Removes this subscription as soon as possible. The subscription might still receive tuple notifications from other threads AFTER this method returns, though. It is impossible to guarantee synchronicity of this method without risking deadlocks, especially with the GUI thread. If you really need to know when the subscription was removed, get in touch with us. We can change the API to provide for a callback.*/
	@Override
	public <T extends Tuple> void removeSubscriptionAsync(Object subscriber) {
		for (Subscription victim : _subscriptions.toArray(SUBSCRIPTION_ARRAY))
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
	public synchronized List<Tuple> keptTuples() {
		return _keptTuples.output().currentElements();
	}


	@Override
	public int transientCacheSize() {
		return TRANSIENT_CACHE_SIZE;
	}

}
