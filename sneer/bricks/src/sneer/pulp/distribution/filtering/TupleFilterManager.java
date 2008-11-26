package sneer.pulp.distribution.filtering;

import sneer.pulp.tuples.Tuple;

public interface TupleFilterManager {

	void block(Class<? extends Tuple> tupleType);

	boolean isBlocked(Tuple tuple);

}
