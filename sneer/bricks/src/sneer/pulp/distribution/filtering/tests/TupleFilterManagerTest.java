package sneer.pulp.distribution.filtering.tests;

import org.junit.Test;

import sneer.kernel.container.Tuple;
import sneer.pulp.distribution.filtering.TupleFilterManager;
import tests.TestInContainerEnvironment;
import static sneer.brickness.Environments.my;

public class TupleFilterManagerTest extends TestInContainerEnvironment {

	private final TupleFilterManager _subject = my(TupleFilterManager.class);

	@Test
	public void testTupleTypeBlocking() {
		assertFalse(_subject.isBlocked(new TupleA()));
		_subject.block(TupleA.class);
		assertTrue(_subject.isBlocked(new TupleA()));

		assertFalse(_subject.isBlocked(new TupleB()));
	}

	@Test
	public void testTupleTypeHierarchyBlocking() {
		assertFalse(_subject.isBlocked(new TupleA()));
		_subject.block(Tuple.class);
		assertTrue(_subject.isBlocked(new TupleA()));
		assertTrue(_subject.isBlocked(new TupleB()));
	}
	
}
