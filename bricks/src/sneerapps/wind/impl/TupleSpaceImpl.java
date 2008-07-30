package sneerapps.wind.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sneer.lego.Inject;
import sneerapps.wind.AffinityManager;
import sneerapps.wind.TupleSpace;
import sneerapps.wind.Tuple;
import wheel.lang.Omnivore;
import wheel.lang.Types;
import static wheel.lang.Types.cast;
import wheel.reactive.Signal;

public class TupleSpaceImpl implements TupleSpace {

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

	private final Set<Object> _tuples = new HashSet<Object>();
	private final List<Subscription> _subscriptions = new ArrayList<Subscription>();

	@Override
	public void publish(Tuple tuple) {
		if (tuple == null) throw new IllegalArgumentException();
		
		if (!_tuples.add(tuple)) return;
		for (Subscription subscription : _subscriptions)
			subscription.filterAndNotify(tuple);
	}

	@Override
	public <T extends Tuple> void addSubscription(Omnivore<T> subscriber,	Class<T> tupleType, Signal<Float> minAffinity) {
		_subscriptions.add(new Subscription(subscriber, tupleType, minAffinity));
	}


}
