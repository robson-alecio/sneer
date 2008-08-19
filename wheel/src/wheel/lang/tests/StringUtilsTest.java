package wheel.lang.tests;

import junit.framework.TestCase;
import wheel.lang.StringUtils;

public class StringUtilsTest extends TestCase {
	
	public void testSplitRight() {
		
		assertSplitRight("", ':', 3, "");
		assertSplitRight(":", ':', 3, "", "");
		assertSplitRight("foo", ':', 1, "foo");
		assertSplitRight("foo", ':', 2, "foo");
		assertSplitRight("foo:bar", ':', 2, "foo", "bar");
		assertSplitRight("foo:bar:baz", ':', 2, "foo:bar", "baz");
		assertSplitRight("foo:bar:baz", ':', 3, "foo" , "bar", "baz");
		assertSplitRight("foo:bar:baz", ':', 4, "foo" , "bar", "baz");
		
	}

	private void assertSplitRight(String s, char separator, int maxParts, String... expected) {
		assertArray(StringUtils.splitRight(s, separator, maxParts), expected);
	}

	public static <T> void assertArray(T[] actual, T... expected) {
		assertEquals(expected.length, actual.length);
		int i=0;
		for (T item : actual) {
			assertEquals("item[" + i + "]", expected[i], item);
			++i;
		}
	}
}
