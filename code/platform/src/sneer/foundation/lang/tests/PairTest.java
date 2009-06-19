package sneer.foundation.lang.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import sneer.foundation.lang.Pair;


public class PairTest {
	
	@Test
	public void testEquals() {
		
		Pair<String, Integer> p1 = Pair.of("ltuae", 42);
		assertEquals(Pair.of("ltuae", 42), p1);
		assertEquals(p1, p1);
		
		assertEquals(Pair.of(null, null), Pair.of(null, null));
		assertFalse(Pair.of(null, 42).equals(Pair.of(42, null)));
		
	}

}
