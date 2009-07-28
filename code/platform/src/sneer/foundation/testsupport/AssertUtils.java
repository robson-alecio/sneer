package sneer.foundation.testsupport;

import org.junit.Assert;

import sneer.foundation.lang.Closure;

public abstract class AssertUtils extends Assert {

	public static void assertFloat(float expected, float actual) {
		Assert.assertEquals(expected, actual, 0.001f);
	}

	public static <T> void assertSameContents(Iterable<T> actual, T... expected) {
		int i = 0;
		for (T actualItem : actual) {
			if (i == expected.length) {
				fail("Unexpected extra element '" + actualItem + "' at index " + i);
			}
			assertEquals("Different values at index " + i, expected[i], actualItem);
			i++;
		}
		assertEquals("Collections not same size", expected.length, i);
	}

	public static void expect(Class<? extends Throwable> throwable, Closure closure) {
		try {
			closure.run();
		} catch (Throwable t) {
			assertTrue(
					"Expecting '" + throwable + "' but got '" + t.getClass() + "'.",
					throwable.isInstance(t));
			return;
		}
		
		fail("Expecting '" + throwable + "'.");
	}

}
