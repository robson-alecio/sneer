package sneer.pulp.probe;

import sneer.pulp.tuples.Tuple;


public interface ProbeManager {

	void blockTupleType(Class<? extends Tuple> type);

}
