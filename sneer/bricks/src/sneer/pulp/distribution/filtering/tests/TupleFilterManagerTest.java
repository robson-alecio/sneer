package sneer.pulp.distribution.filtering.tests;

import org.junit.Test;

import sneer.brickness.Tuple;
import sneer.brickness.testsupport.BrickTest;
import sneer.pulp.distribution.filtering.TupleFilterManager;
import static sneer.brickness.environments.Environments.my;

public class TupleFilterManagerTest extends BrickTest {

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
