package sneer.pulp.distribution.filtering.tests;

import static sneer.brickness.environments.Environments.my;

import org.junit.Test;

import sneer.brickness.Tuple;
import sneer.pulp.config.persistence.testsupport.BrickTest;
import sneer.pulp.distribution.filtering.TupleFilterManager;

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
