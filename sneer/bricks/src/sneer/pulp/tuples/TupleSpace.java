package sneer.pulp.tuples;

import sneer.kernel.container.Brick;
import sneer.pulp.keymanager.PublicKey;
import wheel.lang.Functor;
import wheel.lang.Omnivore;
import wheel.lang.Predicate;

public interface TupleSpace extends Brick {

	void publish(Tuple tuple);

	<T extends Tuple> void addSubscription(Class<T> tupleType, Omnivore<T> subscriber);
	
	<T extends Tuple> void removeSubscription(Class<T> tupleType, Omnivore<T> subscriber);

	<T extends Tuple> Iterable<T> tuples(Class<T> tupleType);

	<T extends Tuple> T mostRecentTuple(Class<T> tupleType, PublicKey publisher, Predicate<? super T> filter);

	<T extends Tuple> Iterable<T> mostRecentTupleByGroup(Class<T> tupleType, PublicKey publisher, Functor<? super T, Object> grouping);

	<T extends Tuple> Iterable<T> mostRecentTupleByGroup(Class<T> tupleType, PublicKey publisher,  Predicate<? super T> filter, Functor<? super T, Object> grouping);

}
