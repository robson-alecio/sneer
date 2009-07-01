package sneer.bricks.pulp.distribution.filtering.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.pulp.distribution.filtering.TupleFilterManager;
import sneer.foundation.brickness.Tuple;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.lang.ByRef;
import sneer.foundation.lang.Predicate;

public class TupleFilterManagerTest extends BrickTest {

	private final TupleFilterManager _subject = my(TupleFilterManager.class);

	@Test
	public void testTupleTypeBlocking() {
		assertTrue(_subject.canBePublished(new TupleA()));
		_subject.block(TupleA.class);
		assertFalse(_subject.canBePublished(new TupleA()));

		assertTrue(_subject.canBePublished(new TupleB()));
	}

	@Test
	public void testTupleTypeHierarchyBlocking() {
		assertTrue(_subject.canBePublished(new TupleA()));
		_subject.block(Tuple.class);
		assertFalse(_subject.canBePublished(new TupleA()));
		assertFalse(_subject.canBePublished(new TupleB()));
	}

	@Test
	public void testCensorship() {
		assertTrue(_subject.canBePublished(new TupleA()));

		final ByRef<Boolean> decision = ByRef.newInstance();
		_subject.setCensor(TupleA.class, new Predicate<TupleA>() { @Override public boolean evaluate(TupleA tuple) {
			return decision.value;
		}});

		decision.value = false;
		assertFalse(_subject.canBePublished(new TupleA()));

		decision.value = true;
		assertTrue(_subject.canBePublished(new TupleA()));
		
		decision.value = false;
		assertTrue(_subject.canBePublished(new TupleB()));

	}
	
}
