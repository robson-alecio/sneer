package sneer.bricks.pulp.tuples.tests;

public class GcTestTuple extends TestTuple {
	public final String _generation;

	GcTestTuple(int intValue_) {
		super(intValue_);
		_generation = TupleGcTest._currentGeneration;
	}

	@Override
	protected void finalize() throws Throwable {
		if (_generation == TupleGcTest._currentGeneration)
			TupleGcTest._garbageCollectedCounter++;
	}
}