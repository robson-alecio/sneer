package sneer.pulp.tuples.tests;

import static wheel.lang.Environments.my;

import org.jmock.Expectations;
import org.junit.Test;

import sneer.pulp.threadpool.mocks.ThreadPoolMock;
import sneer.pulp.tuples.TupleSpace;
import sneer.pulp.tuples.config.TupleSpaceConfig;
import tests.Contribute;
import tests.TestInContainerEnvironment;
import wheel.lang.ByRef;
import wheel.lang.Consumer;

public class TupleSpaceResponsivenessTest extends TestInContainerEnvironment {

	@Contribute private final ThreadPoolMock _threads = new ThreadPoolMock();
	
	@Contribute private final TupleSpaceConfig _tuplesConfig = mock(TupleSpaceConfig.class); {
		checking(new Expectations() {{
			one(_tuplesConfig).isAcquisitionSynchronous();
				will(returnValue(false));
		}});
	}

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
		_threads.stepper(1).step();
		assertTrue(wasPublished.value);
	}
	
}


