package sneerapps.wind;

import sneer.bricks.keymanager.PublicKey;
import wheel.lang.Omnivore;
import wheel.lang.Predicate;
import wheel.reactive.Signal;

public interface TupleSpace {

	void publish(Tuple tuple);

	<T extends Tuple> void addSubscription(Omnivore<T> subscriber, Class<T> tupleType, Signal<Float> minAffinity);

	<T extends Tuple> Iterable<T> tuples(Class<T> tupleType);

	<T extends Tuple> T mostRecentTuple(Class<T> tupleType, PublicKey ownPublicKey, Predicate<T> filter);

}
