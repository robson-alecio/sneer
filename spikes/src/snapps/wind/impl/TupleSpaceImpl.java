package snapps.wind.impl;

import static wheel.lang.Types.cast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import snapps.wind.Tuple;
import snapps.wind.TupleSpace;
import sneer.pulp.keymanager.PublicKey;
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

	private final Set<Tuple> _tuples = new HashSet<Tuple>();
	private final List<Subscription> _subscriptions = new ArrayList<Subscription>();

	@Override
	public void publish(Tuple tuple) {
		if (tuple == null) throw new IllegalArgumentException();
		
		if (!_tuples.add(tuple)) return;
		for (Subscription subscription : _subscriptions)
			subscription.filterAndNotify(tuple);
	}

	@Override
	public <T extends Tuple> void addSubscription(Omnivore<T> subscriber,	Class<T> tupleType) {
		_subscriptions.add(new Subscription(subscriber, tupleType));
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
