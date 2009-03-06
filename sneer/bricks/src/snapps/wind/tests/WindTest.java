package snapps.wind.tests;

import static sneer.brickness.environments.Environments.my;

import org.junit.Test;

import snapps.wind.Shout;
import snapps.wind.Wind;
import sneer.brickness.testsupport.BrickTest;
import sneer.pulp.tuples.TupleSpace;

public class WindTest extends BrickTest {

	private Wind subject() {
		return my(Wind.class);
	}
	
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
		
		Shout previusShout = null;
		for (Shout _shout : subject().shoutsHeard()) {
			
			if(previusShout==null){
				previusShout = _shout;
				continue;
			}
			
			assertTrue(previusShout.publicationTime() < _shout.publicationTime());
			previusShout = _shout;
		}
	}

	private TupleSpace tupleSpace() {
		return my(TupleSpace.class);
	}
}