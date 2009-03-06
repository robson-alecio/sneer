package sneer.pulp.tuples.tests;

import static sneer.brickness.environments.Environments.my;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.brickness.testsupport.Contribute;
import sneer.pulp.config.persistence.mocks.PersistenceConfigMock;
import sneer.pulp.tuples.TupleSpace;
import wheel.lang.Consumer;

public class TupleKeepingTest extends BrickTest {

	@Contribute final PersistenceConfigMock _persistenceConfig = new PersistenceConfigMock(tmpDirectory()); //Refactor: Is this necessary?

	private int _notificationCounter;
	
	
	@Test (timeout = 3000)
	public void tuplesLimitAmount() {

		subject().addSubscription(KeptTuple.class, new Consumer<KeptTuple>() { @Override public void consume(KeptTuple ignored) {
			_notificationCounter++;
		}});
		
		subject().keep(KeptTuple.class);
		subject().publish(new KeptTuple(1));
		flushCache();
		subject().publish(new KeptTuple(1));

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
			subject().publish(new TestTuple(new int[] {i}));
	}
	
}


