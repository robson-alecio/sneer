package sneer.pulp.tuples.tests;

import org.junit.Test;

import sneer.pulp.tuples.TupleSpace;
import tests.TestInContainerEnvironment;
import wheel.lang.Consumer;
import static wheel.lang.Environments.my;

public class TupleKeepingTest extends TestInContainerEnvironment {

	private TupleSpace _subject = my(TupleSpace.class);
	
	private int _notificationCounter;
	
	
	@Test (timeout = 3000)
	public void tuplesLimitAmount() {

		_subject.addSubscription(KeptTuple.class, new Consumer<KeptTuple>() { @Override public void consume(KeptTuple ignored) {
			_notificationCounter++;
		}});
		
		_subject.keep(KeptTuple.class);
		_subject.publish(new KeptTuple(1));
		flushCache();
		_subject.publish(new KeptTuple(1));

		assertEquals(1, _subject.keptTuples().size());
		assertEquals(1, _notificationCounter);
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


