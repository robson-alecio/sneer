package sneer.pulp.distribution.filtering;

import sneer.brickness.Brick;
import sneer.brickness.Tuple;

@Brick
public interface TupleFilterManager {

	void block(Class<? extends Tuple> tupleType);

	boolean isBlocked(Tuple tuple);

}
