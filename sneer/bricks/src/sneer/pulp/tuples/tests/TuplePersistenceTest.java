package sneer.pulp.tuples.tests;

import static sneer.commons.environments.Environments.my;

import java.util.List;

import org.junit.Test;

import sneer.brickness.Tuple;
import sneer.brickness.testsupport.BrickTest;
import sneer.brickness.testsupport.BrickTestRunner;
import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.pulp.tuples.TupleSpace;

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
			assertEquals(0, ((TestTuple)kept.get(0)).intArray[0]);
			assertEquals(1, ((TestTuple)kept.get(1)).intArray[0]);
			assertEquals(2, ((TestTuple)kept.get(2)).intArray[0]);

		}});
	}


	private TestTuple tuple(int i) {
		return new TestTuple(new int[] {i});
	}
	
	private void runInNewEnvironment(Runnable runnable) {
		final Environment newEnvironment = my(BrickTestRunner.class).newTestEnvironment();
		Environments.runWith(newEnvironment, runnable);
	}
	
	private TupleSpace createSubject() {
		return my(TupleSpace.class);
	}
}


