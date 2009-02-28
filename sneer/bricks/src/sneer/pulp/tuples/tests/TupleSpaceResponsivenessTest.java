package sneer.pulp.tuples.tests;

import static sneer.brickness.Environments.my;

import org.junit.Test;

import sneer.brickness.testsupport.Contribute;
import sneer.brickness.testsupport.TestInBrickness;
import sneer.commons.lang.ByRef;
import sneer.pulp.threadpool.mocks.ThreadPoolMock;
import sneer.pulp.tuples.TupleSpace;
import wheel.lang.Consumer;

public class TupleSpaceResponsivenessTest extends TestInBrickness {

	@Contribute private final ThreadPoolMock _threads = new ThreadPoolMock();
	
	private final TupleSpace _subject = my(TupleSpace.class);
	
	@Test (timeout = 1000)
	public void test() {
		final ByRef<Boolean> wasPublished = ByRef.newInstance(false);
		_subject.addSubscription(TestTuple.class, new Consumer<TestTuple>() { @Override public void consume(TestTuple value) {
			wasPublished.value = true;
		}});

		final TestTuple tuple = new TestTuple(42);
		_subject.publish(tuple);
		
		assertFalse(wasPublished.value);
		_threads.stepper(0).step();
		assertTrue(wasPublished.value);
	}
	
}


