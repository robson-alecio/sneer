package sneer.pulp.tuples.tests;

import org.junit.Ignore;
import org.junit.Test;

import sneer.kernel.container.ContainerUtils;
import sneer.pulp.config.persistence.mocks.PersistenceConfigMock;
import sneer.pulp.tuples.TupleSpace;
import wheel.testutil.TestThatMightUseResources;

public class TuplePersistenceTest extends TestThatMightUseResources {

	private final PersistenceConfigMock _persistenceMock  = new PersistenceConfigMock();


	@Ignore
	@Test
	public void testTuplePersistence() {
		TupleSpace subject1 = createSubject();

		assertEquals(0, subject1.keptTuples().size());

		subject1.keep(TestTuple.class);
		subject1.publish(new TestTuple(new int[] {1}));
		subject1.publish(new TestTuple(new int[] {2}));
		subject1.publish(new TestTuple(new int[] {3}));

		subject1.crash();
		
		TupleSpace subject2 = createSubject();
		assertEquals(3, subject2.keptTuples().size());
	}

	
	private TupleSpace createSubject() {
		return ContainerUtils.newContainer(_persistenceMock).produce(TupleSpace.class);
	}
	
	
}


