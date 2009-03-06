package sneer.pulp.tuples.tests;

import static sneer.brickness.environments.Environments.my;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.brickness.testsupport.Contribute;
import sneer.pulp.config.persistence.mocks.PersistenceConfigMock;
import sneer.pulp.tuples.TupleSpace;
import wheel.lang.Threads;

public class TupleGcTest extends BrickTest {

	@Contribute final PersistenceConfigMock _persistenceConfig = new PersistenceConfigMock(tmpDirectory()); //Refactor: Is this necessary?
	
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


