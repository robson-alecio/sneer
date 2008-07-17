package sneerapps.wind;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface Environment {

	void publish(Object tuple);

	<T> void addSubscriber(Omnivore<T> subscriber, Class<T> tupleType, Signal<Float> minAffinity);

}
