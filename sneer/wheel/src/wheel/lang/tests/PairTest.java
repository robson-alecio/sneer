package wheel.lang.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import wheel.lang.Pair;

public class PairTest {
	
	@Test
	public void testEquals() {
		
		Pair<String, Integer> p1 = Pair.pair("ltuae", 42);
		assertEquals(Pair.pair("ltuae", 42), p1);
		assertEquals(p1, p1);
		
		assertEquals(Pair.pair(null, null), Pair.pair(null, null));
		assertFalse(Pair.pair(null, 42).equals(Pair.pair(42, null)));
		
	}

}
