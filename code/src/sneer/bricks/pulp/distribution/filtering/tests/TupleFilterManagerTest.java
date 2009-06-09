package sneer.bricks.pulp.distribution.filtering.tests;

import static sneer.foundation.commons.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.pulp.distribution.filtering.TupleFilterManager;
import sneer.foundation.brickness.Tuple;
import sneer.foundation.brickness.testsupport.BrickTest;

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
