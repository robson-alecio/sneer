package sneer.brickness.testsupport;

import org.junit.Assert;

public class AssertUtils extends Assert {

	public static void assertFloat(float expected, float actual) {
		Assert.assertEquals(expected, actual, 0.001f);
	}

	public static <T> void assertSameContents(Iterable<T> actual, T... expected) {
		int i = 0;
		for (T actualItem : actual) {
			assertEquals("Different values at index " + i, expected[i], actualItem);
			i++;
		}
		assertEquals("Collections not same size", expected.length, i);
	}

}
