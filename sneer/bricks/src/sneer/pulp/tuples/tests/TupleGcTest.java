package sneer.pulp.tuples.tests;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.pulp.tuples.TupleSpace;
import tests.TestThatIsInjected;
import wheel.lang.Threads;

public class TupleGcTest extends TestThatIsInjected {

	@Inject
	private static TupleSpace _subject;
	
	static volatile String _currentGeneration;
	static volatile int _garbageCollectedCounter;
	
	
	@Test (timeout = 2000)
	public void tuplesLimitAmount() {
		_garbageCollectedCounter = 0;

		_currentGeneration = "tuplesLimitAmount";
		publishMyTestTuples(1000 + 42);
		
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


