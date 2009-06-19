package sneer.bricks.pulp.tuples.tests;

import static sneer.foundation.environments.Environments.my;

import java.util.List;

import org.junit.Test;

import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.brickness.Tuple;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.brickness.testsupport.BrickTestRunner;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.Environments;

public class TuplePersistenceTest extends BrickTest {

	@Test
	public void testTuplePersistence() {
		runInNewEnvironment(new Runnable() { @Override public void run() {
			TupleSpace subject1 = createSubject();
	
			assertEquals(0, subject1.keptTuples().size());
	
			subject1.keep(TestTuple.class);
			subject1.publish(tuple(0));
			subject1.publish(tuple(1));
			subject1.publish(tuple(2));
		}});
		
		runInNewEnvironment(new Runnable() { @Override public void run() {
			
			TupleSpace subject2 = createSubject();
			List<Tuple> kept = subject2.keptTuples();
			assertEquals(3, kept.size());
			assertEquals(0, ((TestTuple)kept.get(0)).intValue);
			assertEquals(1, ((TestTuple)kept.get(1)).intValue);
			assertEquals(2, ((TestTuple)kept.get(2)).intValue);

		}});
	}


	private TestTuple tuple(int i) {
		return new TestTuple(i);
	}
	
	private void runInNewEnvironment(Runnable runnable) {
		final Environment newEnvironment = my(BrickTestRunner.class).newTestEnvironment();
		Environments.runWith(newEnvironment, runnable);
	}
	
	private TupleSpace createSubject() {
		return my(TupleSpace.class);
	}
}


