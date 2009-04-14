package sneer.pulp.tuples.impl;

import static sneer.commons.environments.Environments.my;
import static wheel.lang.Types.cast;

import java.io.File;
import java.io.IOException;
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
import sneer.brickness.StoragePath;
import sneer.brickness.Tuple;
import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.pulp.clock.Clock;
import sneer.pulp.exceptionhandling.ExceptionHandler;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.reactive.collections.ListRegister;
import sneer.pulp.reactive.collections.ReactiveCollections;
import sneer.pulp.threadpool.Stepper;
import sneer.pulp.threadpool.ThreadPool;
import sneer.pulp.tuples.TupleSpace;
import sneer.software.lang.Consumer;
import wheel.lang.Threads;

class TupleSpaceImpl implements TupleSpace {

	//Refactor The synchronization will no longer be necessary when the container guarantees synchronization of model bricks.
	class Subscription {

		private final Consumer<? super Tuple> _subscriber;
		private final Class<? extends Tuple> _tupleType;
		private final Environment _environment;
		private final BlockingQueue<Tuple> _tuplesToNotify = new LinkedBlockingQueue<Tuple>(); 

		<T extends Tuple> Subscription(Consumer<? super T> subscriber, Class<T> tupleType) {
			_subscriber = cast(subscriber);
			_tupleType = tupleType;
			_environment = my(Environment.class);
			
			_threads.registerStepper(new Stepper() { @Override public boolean step() {
				Tuple tuple = waitToPopTuple();
				if (tuple != null) notifySubscriber(tuple);
				return true;
			}});

		}

		private void notifySubscriber(final Tuple tuple) {
			_exceptionHandler.shield(new Runnable(){@Override public void run() {
				Environments.runWith(_environment, new Runnable() { @Override public void run() {
					_subscriber.consume(tuple);
				}});
			}});
			dispatchCounterDecrement();
		}

		private Tuple waitToPopTuple() {
			try {
				return _tuplesToNotify.take();
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		}

		void filterAndNotify(final Tuple tuple) {
			if (!_tupleType.isInstance(tuple))
				return;

			dispatchCounterIncrement();
			try {
				_tuplesToNotify.put(tuple);
			} catch (InterruptedException e) {
				throw new IllegalStateException();
			}
		}
	}

	private static final int TRANSIENT_CACHE_SIZE = 1000;
	private static final Subscription[] SUBSCRIPTION_ARRAY = new Subscription[0];

	private final KeyManager _keyManager = my(KeyManager.class);
	private final Clock _clock = my(Clock.class);
	private final StoragePath _persistenceConfig = my(StoragePath.class);
	private final ThreadPool _threads = my(ThreadPool.class);
	private final ExceptionHandler _exceptionHandler = my(ExceptionHandler.class);

	private final List<Subscription> _subscriptions = Collections.synchronizedList(new ArrayList<Subscription>());

	private final Object _dispatchCounterMonitor = new Object();
	private int _dispatchCounter = 0;

	private final Set<Tuple> _transientTupleCache = new LinkedHashSet<Tuple>();
	private final Set<Class<? extends Tuple>> _typesToKeep = new HashSet<Class<? extends Tuple>>();
	private final ListRegister<Tuple> _keptTuples;

	private final Object _publicationMonitor = new Object();



	
	TupleSpaceImpl() {
		_keptTuples = Bubble.wrapStateMachine(prevayler(my(ReactiveCollections.class).newListRegister()));
	}


	private Prevayler prevayler(Object system) {
		PrevaylerFactory factory = prevaylerFactory(system);

		try {
			return factory.create();
		} catch (IOException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (ClassNotFoundException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}


	private PrevaylerFactory prevaylerFactory(Object system) {
		PrevaylerFactory factory = new PrevaylerFactory();
		factory.configurePrevalentSystem(system);
		factory.configurePrevalenceDirectory(directory());
		factory.configureJournalSerializer(new XStreamSerializer());
		factory.configureTransactionFiltering(false);
		return factory;
	}


	private String directory() {
		String dir = _persistenceConfig.get();
		return new File(dir, "tuplespace").getAbsolutePath();
	}

	
	@Override
	public void publish(Tuple tuple) {
		synchronized (_publicationMonitor ) {
			stamp(tuple);
			acquire(tuple);
		}
	}

	@Override
	public synchronized void acquire(Tuple tuple) {
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

	@Override	
	public void waitForAllDispatchingToFinish() {
		synchronized (_dispatchCounterMonitor ) {
			if (_dispatchCounter != 0)
				Threads.waitWithoutInterruptions(_dispatchCounterMonitor);
		}
		
	}

	private void dispatchCounterIncrement() {
		synchronized (_dispatchCounterMonitor ) {
			_dispatchCounter++;
		}
	}

	private void dispatchCounterDecrement() {
		synchronized (_dispatchCounterMonitor ) {
			_dispatchCounter--;
			if (_dispatchCounter == 0)
				_dispatchCounterMonitor.notifyAll();
		}
	}


}
