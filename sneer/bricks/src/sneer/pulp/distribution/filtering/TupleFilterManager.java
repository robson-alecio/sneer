package sneer.pulp.distribution.filtering;

import sneer.brickness.OldBrick;
import sneer.brickness.Tuple;

public interface TupleFilterManager extends OldBrick {

	void block(Class<? extends Tuple> tupleType);

	boolean isBlocked(Tuple tuple);

}
