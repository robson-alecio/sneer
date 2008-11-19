package sneer.pulp.tuples.tests;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.pulp.tuples.TupleSpace;
import tests.TestThatIsInjected;

public class TupleKeepingTest extends TestThatIsInjected {

	@Inject
	private static TupleSpace _subject;
	
	
	@Test (timeout = 3000)
	public void tuplesLimitAmount() {

		_subject.keep(KeptTuple.class);
		_subject.publish(new KeptTuple(1));
		flushCache();
		_subject.publish(new KeptTuple(1));

		assertEquals(1, _subject.keptTuples().size());
	}


	private void flushCache() {
		int cacheSize = _subject.transientCacheSize();
		publishTestTuples(cacheSize);
	}


	private void publishTestTuples(int amount) {
		for (int i = 0; i < amount; i++)
			_subject.publish(new TestTuple(new int[] {i}));
	}
	
}


