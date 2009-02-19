package sneer.pulp.tuples.tests;

import sneer.kernel.container.Tuple;

public class TestTuple extends Tuple {

	public final int[] intArray;
	
	TestTuple(int... pIntArray) {
		intArray = pIntArray;
	}
	
}
