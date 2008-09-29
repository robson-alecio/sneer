package snapps.wind;

import sneer.pulp.tuples.TupleSpace;

public interface Probe {

	void startProbing(TupleSpace foreignEnvironment, ConnectionSide connectionBackHome);

}
