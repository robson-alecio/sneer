package sneer.pulp.distribution.filtering;

import sneer.kernel.container.Brick;
import sneer.kernel.container.Tuple;

public interface TupleFilterManager extends Brick {

	void block(Class<? extends Tuple> tupleType);

	boolean isBlocked(Tuple tuple);

}
