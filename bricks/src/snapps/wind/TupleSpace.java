package snapps.wind;

import sneer.pulp.keymanager.PublicKey;
import wheel.lang.Functor;
import wheel.lang.Omnivore;
import wheel.lang.Predicate;

public interface TupleSpace {

	void publish(Tuple tuple);

	<T extends Tuple> void addSubscription(Omnivore<T> subscriber, Class<T> tupleType);

	<T extends Tuple> Iterable<T> tuples(Class<T> tupleType);

	<T extends Tuple> T mostRecentTuple(Class<T> tupleType, PublicKey publisher, Predicate<? super T> filter);

	<T extends Tuple> Iterable<T> mostRecentTupleByGroup(Class<T> tupleType, PublicKey publisher, Functor<? super T, Object> grouping);

	<T extends Tuple> Iterable<T> mostRecentTupleByGroup(Class<T> tupleType, PublicKey publisher,  Predicate<? super T> filter, Functor<? super T, Object> grouping);

}
