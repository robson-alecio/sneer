package sneer.pulp.tuples.tests;

import static sneer.commons.environments.Environments.my;

import java.util.ArrayList;

import org.junit.Test;

import sneer.brickness.Tuple;
import sneer.brickness.testsupport.BrickTest;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.tuples.TupleSpace;

public class TupleSpaceTest extends BrickTest {

	private final TupleSpace _subject = my(TupleSpace.class);
	
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


