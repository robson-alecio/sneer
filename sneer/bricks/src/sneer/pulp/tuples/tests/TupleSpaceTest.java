package sneer.pulp.tuples.tests;

import java.util.ArrayList;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.tuples.Tuple;
import sneer.pulp.tuples.TupleSpace;
import wheel.lang.Omnivore;
import wheel.lang.Threads;

public class TupleSpaceTest extends TestThatIsInjected {

	@Inject
	private static TupleSpace _subject;
	
	private TestTuple _received;

	static volatile String _currentGeneration;
	static volatile int _garbageCollectedCounter;
	
	
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

	@Test
	public void keepingTuples() {
		_garbageCollectedCounter = 0;
		
		_subject.keep(MyTestTuple.class);

		_currentGeneration = "keepingTuples";
		publishMyTestTuples(1000 + 42);

		System.gc();
		assertEquals(0, _garbageCollectedCounter);
		
		assertEquals(1042, _subject.keptTuples().size());
	}


	private void publishMyTestTuples(int amount) {
		for (int i = 0; i < amount; i++)
			_subject.publish(new MyTestTuple(new int[] {i}));
	}
	
	
	@Test
	public void removeSubscription() {
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


