package sneer.bricks.pulp.distribution.filtering;

import sneer.foundation.brickness.Brick;
import sneer.foundation.brickness.Tuple;

@Brick
public interface TupleFilterManager {

	void block(Class<? extends Tuple> tupleType);

	boolean isBlocked(Tuple tuple);

}
