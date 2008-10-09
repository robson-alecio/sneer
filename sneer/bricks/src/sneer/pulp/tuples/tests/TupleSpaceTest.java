package sneer.pulp.tuples.tests;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
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
	
}
