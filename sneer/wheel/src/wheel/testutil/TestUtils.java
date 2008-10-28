package wheel.testutil;

import junit.framework.Assert;

public class TestUtils {

	public static void assertFloat(float expected, float actual) {
		Assert.assertEquals(expected, actual, 0.001f);
	}

	public static <T> void assertSameContents(Iterable<T> actual, T... expected) {
		
		int i=0;
		for (T actualItem : actual) {
			Assert.assertEquals("different values at index " + i, actualItem, expected[i]);
			++i;
		}
		Assert.assertEquals(expected.length, i);
	}

}
