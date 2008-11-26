package sneer.pulp.distribution.filtering.tests;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.pulp.distribution.filtering.TupleFilterManager;
import sneer.pulp.tuples.Tuple;
import tests.TestThatIsInjected;

public class TupleFilterManagerTest extends TestThatIsInjected {

	@Inject
	private static TupleFilterManager _subject;

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
