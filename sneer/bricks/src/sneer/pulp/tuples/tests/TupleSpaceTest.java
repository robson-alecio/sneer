package sneer.pulp.tuples.tests;

import static sneer.brickness.environments.Environments.my;

import java.util.ArrayList;

import org.junit.Test;

import sneer.brickness.Tuple;
import sneer.brickness.testsupport.BrickTest;
import sneer.brickness.testsupport.Contribute;
import sneer.pulp.config.persistence.mocks.PersistenceConfigMock;
import sneer.pulp.tuples.TupleSpace;
import wheel.lang.Consumer;

public class TupleSpaceTest extends BrickTest {

	@Contribute final PersistenceConfigMock _persistenceConfig = new PersistenceConfigMock(tmpDirectory()); //Refactor: Is this necessary?

	private final TupleSpace _subject = my(TupleSpace.class);
	
	private TestTuple _received;

	
	@Test
	public void tuplesContainingArrays() {
		TestTuple a = new TestTuple(new int[]{1, 2, 3});
		TestTuple b = new TestTuple(new int[]{1, 2, 3});
		assertTrue(a.hashCode() == b.hashCode());
		assertEquals(a, b);

		_subject.addSubscription(TestTuple.class, new Consumer<TestTuple>(){@Override public void consume(TestTuple received) {
			_received = received;
		}});
		
		_subject.publish(a);
		_subject.waitForAllDispatchingToFinish();
		assertEquals(_received, a);
		
		_received = null;
		_subject.publish(b);
		assertNull(_received);
	}
	
	
	@Test
	public void subscriptionRemoval() {
		final ArrayList<Tuple> tuples = new ArrayList<Tuple>();
		final Consumer<TestTuple> consumer = new Consumer<TestTuple>() { @Override public void consume(TestTuple value) {
			tuples.add(value);
		}};
		_subject.addSubscription(TestTuple.class, consumer);
		
		final TestTuple tuple = new TestTuple(42);
		_subject.publish(tuple);
		_subject.removeSubscriptionAsync(consumer);
		_subject.publish(new TestTuple(-1));
		my(TupleSpace.class).waitForAllDispatchingToFinish();
		assertArrayEquals(new Object[] { tuple }, tuples.toArray());
	}
	
}


