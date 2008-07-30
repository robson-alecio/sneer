package sneerapps.wind;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface TupleSpace {

	void publish(Tuple tuple);

	<T extends Tuple> void addSubscription(Omnivore<T> subscriber, Class<T> tupleType, Signal<Float> minAffinity);

}
