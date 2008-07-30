package wheel.testutil;

import junit.framework.Assert;

public class TestUtils {

	public static void assertFloat(float expected, float actual) {
		Assert.assertEquals(expected, actual, 0.001f);
	}

}
