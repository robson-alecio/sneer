package sneer.bricks.pulp.tuples.tests;

import static sneer.foundation.commons.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.brickness.testsupport.BrickTest;

public class TupleKeepingTest extends BrickTest {

	private int _notificationCounter;
	
	
	@Test (timeout = 5000)
	public void tuplesLimitAmount() {

		Consumer<KeptTuple> consumerToAvoidGc = new Consumer<KeptTuple>() { @Override public void consume(KeptTuple ignored) {
			_notificationCounter++;
		}};
		subject().addSubscription(KeptTuple.class, consumerToAvoidGc);
		
		subject().keep(KeptTuple.class);
		subject().publish(new KeptTuple(1));
		flushCache();
		subject().publish(new KeptTuple(1));
		
		subject().waitForAllDispatchingToFinish();

		assertEquals(1, subject().keptTuples().size());
		assertEquals(1, _notificationCounter);
	}


	private TupleSpace subject() {
		return my(TupleSpace.class);
	}


	private void flushCache() {
		int cacheSize = subject().transientCacheSize();
		publishTestTuples(cacheSize);
	}


	private void publishTestTuples(int amount) {
		for (int i = 0; i < amount; i++)
			subject().publish(new TestTuple(i));
	}
	
}


