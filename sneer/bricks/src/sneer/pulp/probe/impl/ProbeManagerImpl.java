package sneer.pulp.probe.impl;

import sneer.kernel.container.Inject;
import sneer.pulp.probe.ProbeManager;
import sneer.pulp.tuples.Tuple;
import sneer.pulp.tuples.TupleSpace;
import wheel.lang.Omnivore;

public class ProbeManagerImpl implements ProbeManager, Omnivore<Tuple> {

	@Inject
	static private TupleSpace _tuples;
	
	{
		_tuples.addSubscription(this, Tuple.class);
	}

	@Override
	public void consume(Tuple value) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}
	
}
