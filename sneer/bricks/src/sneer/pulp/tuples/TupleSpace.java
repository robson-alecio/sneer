package sneer.pulp.tuples;

import sneer.kernel.container.Brick;
import wheel.lang.Omnivore;

public interface TupleSpace extends Brick {

	void publish(Tuple tuple);

	<T extends Tuple> void addSubscription(Class<T> tupleType, Omnivore<T> subscriber);
	<T extends Tuple> void removeSubscription(Class<T> tupleType, Omnivore<T> subscriber);

	long tupleCount();
	Tuple tuple(long index);


}
