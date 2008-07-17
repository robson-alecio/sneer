package sneerapps.wind.tests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sneerapps.wind.Environment;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class EnvironmentMock implements Environment {

	static class Subscription {

		private final Omnivore<Object> _subscriber;
		private final Class<?> _tupleType;
		private final Signal<Float> _minAffinity;

		Subscription(Omnivore<Object> subscriber, Class<?> tupleType, Signal<Float> minAffinity) {
			_subscriber = subscriber;
			_tupleType = tupleType;
			_minAffinity = minAffinity;
		}

		void filterAndNotify(Object tuple) {
			_subscriber.consume(tuple);
		}

	}

	private final Set<Object> _tuples = new HashSet<Object>();
	private final List<Subscription> _subscriptions = new ArrayList<Subscription>();

	@Override
	public void publish(Object tuple) {
		if (!_tuples.add(tuple)) return;
		for (Subscription subscription : _subscriptions)
			subscription.filterAndNotify(tuple);
	}

	@Override
	public <T> void addSubscriber(Omnivore<T> subscriber, Class<T> tupleType,	Signal<Float> minAffinity) {
		_subscriptions.add(new Subscription((Omnivore<Object>)subscriber, tupleType, minAffinity));
	}

}
