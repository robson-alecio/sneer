package sneer.pulp.probe.tests;

import sneer.kernel.container.Tuple;

public abstract class TupleWithId extends Tuple {

	public final int id;

	public TupleWithId(int id_) {
		id = id_;
	}

}