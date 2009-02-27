package sneer.pulp.tuples.tests;

import org.junit.Test;

import sneer.brickness.testsupport.TestInContainerEnvironment;
import sneer.pulp.tuples.TupleSpace;
import wheel.lang.Threads;
import static sneer.brickness.Environments.my;

public class TupleGcTest extends TestInContainerEnvironment {

	private final TupleSpace _subject = my(TupleSpace.class);
	
	static volatile String _currentGeneration;
	static volatile int _garbageCollectedCounter;
	
	
	@Test (timeout = 2000)
	public void tuplesLimitAmount() {
		_garbageCollectedCounter = 0;

		_currentGeneration = "tuplesLimitAmount";
		int cache = _subject.transientCacheSize();
		publishMyTestTuples(cache + 42);
		
		while (_garbageCollectedCounter != 42) {
			System.gc();
			Threads.sleepWithoutInterruptions(20);
		}
	}


	private void publishMyTestTuples(int amount) {
		for (int i = 0; i < amount; i++)
			_subject.publish(new GcTestTuple(new int[] {i}));
	}
	
}


