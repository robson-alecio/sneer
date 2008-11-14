package sneer.pulp.tuples;

import java.util.Collection;

import sneer.kernel.container.Brick;
import wheel.lang.Consumer;

public interface TupleSpace extends Brick {

	void publish(Tuple newOrignalTupleByTheKing);
	void acquire(Tuple someTupleThatCameFromAContact);

	<T extends Tuple> void addSubscription(Class<T> tupleType, Consumer<? super T> subscriber);
	<T extends Tuple> void removeSubscription(Object subscriber);
	
	void keep(Class<? extends Tuple> tupleType);
	Collection<Tuple> keptTuples();
	
	void crash();

}
