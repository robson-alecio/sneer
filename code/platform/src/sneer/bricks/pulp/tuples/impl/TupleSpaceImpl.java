package sneer.bricks.pulp.tuples.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.foundation.serialization.Serializer;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.cpu.threads.Stepper;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.pulp.exceptionhandling.ExceptionHandler;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.reactive.collections.CollectionSignals;
import sneer.bricks.pulp.reactive.collections.ListRegister;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.bricks.software.directoryconfig.DirectoryConfig;
import sneer.foundation.brickness.Seal;
import sneer.foundation.brickness.Tuple;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.Environments;
import sneer.foundation.lang.Consumer;

class TupleSpaceImpl implements TupleSpace {

	//Refactor The synchronization will no longer be necessary when the container guarantees synchronization of model bricks.
	class Subscription {

		private final WeakReference<Consumer<? super Tuple>> _subscriber;
		private final Class<? extends Tuple> _tupleType;
		private final Environment _environment;
		private final List<Tuple> _tuplesToNotify = new LinkedList<Tuple>(); 

		<T extends Tuple> Subscription(Consumer<? super T> subscriber, Class<T> tupleType) {
			_subscriber = new WeakReference<Consumer<? super Tuple>>((Consumer<? super Tuple>) subscriber);
			_tupleType = tupleType;
			_environment = my(Environment.class);
			
			_threads.registerStepper(notifier());

		}

		private Stepper notifier() {
			return new Stepper() { @Override public boolean step() {
				Tuple tuple = waitToPopTuple();
				if (tuple == null) return true; //Is testing for null really necessary?
				
				Consumer<? super Tuple> subscriber = _subscriber.get();
				if (subscriber == null) return false;
				
				notifySubscriber(subscriber, tuple);
				return true;
			}};
		}

		private void notifySubscriber(final Consumer<? super Tuple> subscriber, final Tuple tuple) {
			_exceptionHandler.shield(new Runnable(){@Override public void run() {
				Environments.runWith(_environment, new Runnable() { @Override public void run() {
					subscriber.consume(tuple);
				}});
			}});
			dispatchCounterDecrement();
		}

		void filterAndNotify(final Tuple tuple) {
			if (!_tupleType.isInstance(tuple))
				return;
			
			dispatchCounterIncrement();
			pushTuple(tuple);
		}
		
		private Tuple waitToPopTuple() {
			synchronized (_tuplesToNotify) {
				if (_tuplesToNotify.isEmpty())
					my(Threads.class).waitWithoutInterruptions(_tuplesToNotify);
				
				return _tuplesToNotify.remove(0);
			}
		}

		private void pushTuple(Tuple tuple) {
			synchronized (_tuplesToNotify) {
				_tuplesToNotify.add(tuple);
				_tuplesToNotify.notify();
			}
		}

		private boolean wasGcd() {
			return _subscriber.get() == null;
		}
	}

	private static final int TRANSIENT_CACHE_SIZE = 1000;
	private static final Subscription[] SUBSCRIPTION_ARRAY = new Subscription[0];

	private final Seals _keyManager = my(Seals.class);
	private final Threads _threads = my(Threads.class);
	private final ExceptionHandler _exceptionHandler = my(ExceptionHandler.class);

	private final List<Subscription> _subscriptions = Collections.synchronizedList(new ArrayList<Subscription>());

	private final Object _dispatchCounterMonitor = new Object();
	private int _dispatchCounter = 0;

	private final Set<Tuple> _transientTupleCache = new LinkedHashSet<Tuple>();
	private final Set<Class<? extends Tuple>> _typesToKeep = new HashSet<Class<? extends Tuple>>();
	private final ListRegister<Tuple> _keptTuples;

	private final Object _publicationMonitor = new Object();



	
	TupleSpaceImpl() {
		_keptTuples = Bubble.wrapStateMachine(prevayler(my(CollectionSignals.class).newListRegister()));
		
		for (Tuple tuple : _keptTuples.output().currentElements())
			if (isWeird(tuple)) _keptTuples.remove(tuple);
	}


	private Prevayler prevayler(Object system) {
		PrevaylerFactory factory = prevaylerFactory(system);

		try {
			return factory.create();
		} catch (IOException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (ClassNotFoundException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}


	private PrevaylerFactory prevaylerFactory(Object system) {
		PrevaylerFactory factory = new PrevaylerFactory();
		factory.configurePrevalentSystem(system);
		factory.configurePrevalenceDirectory(directory().getAbsolutePath());
		factory.configureTransactionFiltering(false);
		factory.configureJournalSerializer("xstreamjournal", new Serializer(){
			@Override public Object readObject(InputStream stream) throws IOException, ClassNotFoundException {
				return my(sneer.bricks.pulp.serialization.Serializer.class).deserialize(stream, TupleSpaceImpl.class.getClassLoader());
			}

			@Override public void writeObject(OutputStream stream, Object object) throws IOException {
				my(sneer.bricks.pulp.serialization.Serializer.class).serialize(stream, object);
			}
		});
		return factory;
	}


	private File directory() {
		return my(DirectoryConfig.class).getStorageDirectoryFor(TupleSpace.class);
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
		if (isWeird(tuple)) return; //Filter out those weird shouts that appeared in the beginning.

		if (!_transientTupleCache.add(tuple)) return;
		capTransientTuples();
		
		if (isAlreadyKept(tuple)) return;
		keepIfNecessary(tuple);
				
		notifySubscriptions(tuple);
	}


	private boolean isWeird(Tuple tuple) {
		if (nameFor(tuple.publisher()).length() < 60) return false;
		
		my(BlinkingLights.class).turnOn(LightType.ERROR, "Weird Tuple", "Tuples with weird publishers are being filtered out.", 30000);
		return true;
	}

	private String nameFor(Seal seal) {
		try {
			return new String(seal.bytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}


	private void notifySubscriptions(Tuple tuple) {
		for (Subscription subscription : _subscriptions.toArray(SUBSCRIPTION_ARRAY)) {
			if (subscription.wasGcd()) {
				_subscriptions.remove(subscription);
				my(Logger.class).log("Subscriber gc'd for tupleType: " + subscription._tupleType);
				continue;
			}
			subscription.filterAndNotify(tuple);
		}
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
		tuple.stamp(_keyManager.ownSeal(), my(Clock.class).time());
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
			if (victim._subscriber.get() == subscriber) {
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
			while (_dispatchCounter != 0)
				_threads.waitWithoutInterruptions(_dispatchCounterMonitor);
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
