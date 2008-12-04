package sneer.pulp.tuples.tests;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.pulp.threadpool.mocks.ThreadPoolMock;
import sneer.pulp.tuples.TupleSpace;
import sneer.pulp.tuples.config.TupleSpaceConfig;
import tests.Contribute;
import tests.TestThatIsInjected;
import wheel.lang.ByRef;
import wheel.lang.Consumer;

public class TupleSpaceResponsivenessTest extends TestThatIsInjected {

	@Inject private static TupleSpace _subject;
	
	@Contribute private final ThreadPoolMock _threads = new ThreadPoolMock();
	
	private final Mockery _mockery = new JUnit4Mockery();
	
	@Contribute private final TupleSpaceConfig _tuplesConfig = _mockery.mock(TupleSpaceConfig.class); {
		_mockery.checking(new Expectations() {{
			one(_tuplesConfig).isAcquisitionSynchronous();
				will(returnValue(false));
		}});
	}

	@Test (timeout = 1000)
	public void test() {
		final ByRef<Boolean> wasPublished = ByRef.newInstance(false);
		final Consumer<TestTuple> consumer = new Consumer<TestTuple>() { @Override public void consume(TestTuple value) {
			wasPublished.value = true;
		}};
		
		_subject.addSubscription(TestTuple.class, consumer);

		final TestTuple tuple = new TestTuple(42);
		_subject.publish(tuple);
		
		assertFalse(wasPublished.value);
		_threads.stepper(0).step();
		assertTrue(wasPublished.value);
	}
	
}


