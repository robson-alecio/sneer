package sneer.pulp.distribution.filtering;

import sneer.brickness.Brick;
import sneer.brickness.Tuple;

public interface TupleFilterManager extends Brick {

	void block(Class<? extends Tuple> tupleType);

	boolean isBlocked(Tuple tuple);

}
