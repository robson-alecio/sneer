package sneer.pulp.distribution.filtering;

import sneer.kernel.container.Tuple;

public interface TupleFilterManager {

	void block(Class<? extends Tuple> tupleType);

	boolean isBlocked(Tuple tuple);

}
