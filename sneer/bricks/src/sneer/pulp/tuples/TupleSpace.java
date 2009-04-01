package sneer.pulp.tuples;

import java.util.List;

import sneer.brickness.OldBrick;
import sneer.brickness.Tuple;
import wheel.lang.Consumer;

public interface TupleSpace extends OldBrick {

	void publish(Tuple newOrignalTupleByTheKing);
	void acquire(Tuple someTupleThatCameFromAContact);

	<T extends Tuple> void addSubscription(Class<T> tupleType, Consumer<? super T> subscriber);
	<T extends Tuple> void removeSubscriptionAsync(Object subscriber);
	
	int transientCacheSize();

	void keep(Class<? extends Tuple> tupleType);
	List<Tuple> keptTuples();
	
	void waitForAllDispatchingToFinish();
	
}
