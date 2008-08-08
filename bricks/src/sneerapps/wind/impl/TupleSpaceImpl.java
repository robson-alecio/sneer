package sneerapps.wind.impl;

import static wheel.lang.Types.cast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.keymanager.PublicKey;
import sneer.lego.Inject;
import sneerapps.wind.AffinityManager;
import sneerapps.wind.Tuple;
import sneerapps.wind.TupleSpace;
import wheel.lang.Functor;
import wheel.lang.Nulls;
import wheel.lang.Omnivore;
import wheel.lang.Predicate;
import wheel.lang.Types;
import wheel.reactive.Signal;

public class TupleSpaceImpl implements TupleSpace {

	@Inject
	static private KeyManager _keyManager;

	static class Subscription {

		@Inject
		static private AffinityManager _affinityManager;
	
		private final Omnivore<Tuple> _subscriber;
		private final Class<? extends Tuple> _tupleType;
		private final Signal<Float> _minAffinity;

		<T extends Tuple> Subscription(Omnivore<T> subscriber, Class<T> tupleType, Signal<Float> minAffinity) {
			_subscriber = cast(subscriber);
			_tupleType = tupleType;
			_minAffinity = minAffinity;
		}

		void filterAndNotify(Tuple tuple) {
			if (!Types.instanceOf(tuple, _tupleType))
				return;

			if (!checkAffinity(tuple))
				return;
			
			_subscriber.consume(tuple);
		}

		private boolean checkAffinity(Tuple candidate) {
			Float candidateAffinity = _affinityManager.affinityFor(candidate.publisher);
			return candidateAffinity >= _minAffinity.currentValue();
		}

	}

	private final Set<Tuple> _tuples = new HashSet<Tuple>();
	private final List<Subscription> _subscriptions = new ArrayList<Subscription>();

	@Override
	public void publish(Tuple tuple) {
		if (tuple == null) throw new IllegalArgumentException();
		
		if (!_tuples.add(tuple)) return;
		System.out.println(tuple + " at " + _keyManager.ownPublicKey() );
		for (Subscription subscription : _subscriptions)
			subscription.filterAndNotify(tuple);
	}

	@Override
	public <T extends Tuple> void addSubscription(Omnivore<T> subscriber,	Class<T> tupleType, Signal<Float> minAffinity) {
		_subscriptions.add(new Subscription(subscriber, tupleType, minAffinity));
	}


	@Override
	public <T extends Tuple> Iterable<T> tuples(Class<T> tupleType) {
		Collection<T> result = new ArrayList<T>();
		
		for (Tuple candidate : _tuples)
			if (Types.instanceOf(candidate, tupleType)) {
				T casted = cast(candidate);
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
