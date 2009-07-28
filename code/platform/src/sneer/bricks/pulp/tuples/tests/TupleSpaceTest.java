package sneer.bricks.pulp.tuples.tests;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;

import org.junit.Test;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.brickness.Tuple;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.lang.Consumer;

public class TupleSpaceTest extends BrickTest {

	private final TupleSpace _subject = my(TupleSpace.class);
	
	@Test (timeout = 2000)
	public void subscriptionRemoval() {
		final ArrayList<Tuple> tuples = new ArrayList<Tuple>();
		WeakContract contract = _subject.addSubscription(TestTuple.class, new Consumer<TestTuple>() { @Override public void consume(TestTuple value) {
			tuples.add(value);
		}});
		
		final TestTuple tuple = new TestTuple(42);
		_subject.publish(tuple);
		my(TupleSpace.class).waitForAllDispatchingToFinish();

		contract.dispose();
		
		_subject.publish(new TestTuple(-1));
		my(TupleSpace.class).waitForAllDispatchingToFinish();
		assertArrayEquals(new Object[] { tuple }, tuples.toArray());
	}
	
}


