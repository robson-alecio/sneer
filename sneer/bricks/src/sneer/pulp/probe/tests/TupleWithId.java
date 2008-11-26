package sneer.pulp.probe.tests;

import sneer.pulp.tuples.Tuple;

public abstract class TupleWithId extends Tuple {

	public final int id;

	public TupleWithId(int id_) {
		id = id_;
	}

}