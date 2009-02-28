package sneer.pulp.distribution.filtering.tests;

import org.junit.Test;

import sneer.brickness.testsupport.TestInBrickness;
import sneer.kernel.container.Tuple;
import sneer.pulp.distribution.filtering.TupleFilterManager;
import static sneer.brickness.Environments.my;

public class TupleFilterManagerTest extends TestInBrickness {

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
