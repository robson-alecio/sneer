package sneer.pulp.tuples.impl;

import static wheel.lang.Types.cast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.Tuple;
import sneer.pulp.tuples.TupleSpace;
import wheel.lang.Functor;
import wheel.lang.Nulls;
import wheel.lang.Omnivore;
import wheel.lang.Predicate;
import wheel.lang.Types;

public class TupleSpaceImpl implements TupleSpace {

	static class Subscription {

		private final Omnivore<Tuple> _subscriber;
		private final Class<? extends Tuple> _tupleType;

		<T extends Tuple> Subscription(Omnivore<T> subscriber, Class<T> tupleType) {
			_subscriber = cast(subscriber);
			_tupleType = tupleType;
		}

		void filterAndNotify(Tuple tuple) {
			if (!Types.instanceOf(tuple, _tupleType))
				return;
			
			_subscriber.consume(tuple);
		}


	}

	private static final int TUPLE_SPACE_SIZE_LIMIT = 1000;
	private final Set<Tuple> _tuples = Collections.synchronizedSet(new LinkedHashSet<Tuple>()); //Refactor This synchronization will no longer be necessary when the container guarantees synchronization of model bricks.
	private final List<Subscription> _subscriptions = new ArrayList<Subscription>();

	@Override
	public void publish(Tuple tuple) {
		if (tuple == null) throw new IllegalArgumentException();
		
		if (!_tuples.add(tuple)) return;

		capSize();
		
		for (Subscription subscription : _subscriptions)
			subscription.filterAndNotify(tuple);
	}

	private void capSize() {
		if (_tuples.size() <= TUPLE_SPACE_SIZE_LIMIT) return;
		
		synchronized (_tuples) {  //Refactor This synchronization will no longer be necessary when the container guarantees synchronization of model bricks.
			Iterator<Tuple> tuplesIterator = _tuples.iterator();
			tuplesIterator.next();
			tuplesIterator.remove();
		}
	}

	@Override
	public <T extends Tuple> void addSubscription(Class<T> tupleType,	Omnivore<T> subscriber) {
		_subscriptions.add(new Subscription(subscriber, tupleType));
	}
	
	@Override
	public <T extends Tuple> void removeSubscription(Class<T> tupleType,	Omnivore<T> subscriber) {
		final Iterator<Subscription> iterator = _subscriptions.iterator();
		while (iterator.hasNext()) {
			final Subscription current = iterator.next();
			if (current._tupleType == tupleType
				&& current._subscriber == subscriber) {
				iterator.remove();
				break;
			}
		}
	}

	@Override
	public <T extends Tuple> Iterable<T> tuples(Class<T> tupleType) {
		Collection<T> result = new ArrayList<T>();
		
		for (Tuple candidate : _tuples)
			if (Types.instanceOf(candidate, tupleType)) {
				T casted = (T)(candidate);
				result.add(casted);
			}
		
		return result;
	}


	@Override
	public <T extends Tuple> T mostRecentTuple(Class<T> tupleType, PublicKey publisher, Predicate<? super T> filter) {
		Iterable<T> qq = mostRecentTupleByGroup(tupleType, publisher, filter, Functor.SINGLETON_FUNCTOR);
		return Nulls.firstOrNull(qq);
	}


	@Override
	public <T extends Tuple> Iterable<T> mostRecentTupleByGroup(Class<T> tupleType, PublicKey publisher, Functor<? super T, Object> grouping) {
		return mostRecentTupleByGroup(tupleType, publisher, Predicate.TRUE, grouping);
	}

	@Override
	public <T extends Tuple> Iterable<T> mostRecentTupleByGroup(Class<T> tupleType, PublicKey publisher, Predicate<? super T> filter, Functor<? super T, Object> grouping) {
		Map<Object, T> result = new HashMap<Object, T>();
		
		for (T candidate : tuples(tupleType)) {
			if (!candidate.publisher.equals(publisher)) continue;
			if (!filter.evaluate(candidate)) continue;

			Object group = grouping.evaluate(candidate);
			
			T mostRecentSoFar = 	result.get(group);
			if (mostRecentSoFar == null || candidate.publicationTime > mostRecentSoFar.publicationTime)
				result.put(group, candidate);
		}
		
		return result.values();
	}


}
