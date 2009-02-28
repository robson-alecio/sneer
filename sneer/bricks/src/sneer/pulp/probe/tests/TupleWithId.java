package sneer.pulp.probe.tests;

import sneer.brickness.Tuple;

public abstract class TupleWithId extends Tuple {

	public final int id;

	public TupleWithId(int id_) {
		id = id_;
	}

}