package snapps.wind.tests;

import static sneer.commons.environments.Environments.my;

import org.junit.Test;

import snapps.wind.Shout;
import snapps.wind.Wind;
import sneer.brickness.testsupport.BrickTest;
import sneer.pulp.tuples.TupleSpace;

public class WindTest extends BrickTest {

	private final Wind _subject =  my(Wind.class);
	
	@Test
	public void testSortedShoutsHeard() {
		
		tupleSpace().publish(new ShoutMock(""+15, 15));

		for (int i = 30; i > 20; i--) {
			ShoutMock shout = new ShoutMock(""+i, i);
			tupleSpace().publish(shout);
		}
		
		for (int i = 10; i > 0; i--) {
			ShoutMock shout = new ShoutMock(""+i, i);
			tupleSpace().publish(shout);
		}

		tupleSpace().waitForAllDispatchingToFinish();
		Shout previousShout = null;
		for (Shout _shout : _subject.shoutsHeard()) {
			
			if(previousShout==null){
				previousShout = _shout;
				continue;
			}
			
			assertTrue(previousShout.publicationTime() < _shout.publicationTime());
			previousShout = _shout;
		}

		assertEquals(21, _subject.shoutsHeard().currentSize());
	}

	private TupleSpace tupleSpace() {
		return my(TupleSpace.class);
	}
}