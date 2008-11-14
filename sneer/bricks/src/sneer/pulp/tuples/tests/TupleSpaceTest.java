package sneer.pulp.tuples.tests;

import java.util.ArrayList;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.tuples.Tuple;
import sneer.pulp.tuples.TupleSpace;
import wheel.lang.Omnivore;

public class TupleSpaceTest extends TestThatIsInjected {

	@Inject
	private static TupleSpace _subject;
	
	private TestTuple _received;

	
	@Test
	public void tuplesContainingArrays() {
		TestTuple a = new TestTuple(new int[]{1, 2, 3});
		TestTuple b = new TestTuple(new int[]{1, 2, 3});
		assertTrue(a.hashCode() == b.hashCode());
		assertEquals(a, b);

		_subject.addSubscription(TestTuple.class, new Omnivore<TestTuple>(){@Override public void consume(TestTuple received) {
			_received = received;
		}});
		
		_subject.publish(a);
		assertEquals(_received, a);
		
		_received = null;
		_subject.publish(b);
		assertNull(_received);
	}
	
	
	@Test
	public void subscriptionRemoval() {
		final ArrayList<Tuple> tuples = new ArrayList<Tuple>();
		final Omnivore<TestTuple> consumer = new Omnivore<TestTuple>() {
			@Override
			public void consume(TestTuple value) {
				tuples.add(value);
			}
		};
		_subject.addSubscription(TestTuple.class, consumer);
		final TestTuple tuple = new TestTuple(42);
		_subject.publish(tuple);
		_subject.removeSubscription(consumer);
		_subject.publish(new TestTuple(-1));
		assertArrayEquals(new Object[] { tuple }, tuples.toArray());
	}
	
}


