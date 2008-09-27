package wheel.testutil;

import java.util.List;

import junit.framework.Assert;

public class TestUtils {

	public static void assertFloat(float expected, float actual) {
		Assert.assertEquals(expected, actual, 0.001f);
	}

	public static <T> void assertListEquals(List<T> actual, T... expected) {
		Assert.assertEquals(expected.length, actual.size());
		
		int i=0;
		for (T actualItem : actual) {
			Assert.assertEquals("different values at index " + i, actualItem, expected[i]);
			++i;
		}
	}

}
