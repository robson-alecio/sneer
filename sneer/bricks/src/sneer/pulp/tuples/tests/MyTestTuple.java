package sneer.pulp.tuples.tests;

public class MyTestTuple extends TestTuple {
	public final String _generation;

	MyTestTuple(int[] intArray_) {
		super(intArray_);
		_generation = TupleSpaceTest._currentGeneration;
	}

	@Override
	protected void finalize() throws Throwable {
		if (_generation == TupleSpaceTest._currentGeneration)
			TupleSpaceTest._garbageCollectedCounter++;
	}
}