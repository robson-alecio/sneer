package spikes.wheel.lang.tests;

import static sneer.foundation.commons.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.pulp.lang.StringUtils;
import sneer.foundation.brickness.testsupport.BrickTest;

public class StringUtilsTest extends BrickTest {

	@Test
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

	private static void assertSplitRight(String s, char separator, int maxParts, String... expected) {
		assertArray(my(StringUtils.class).splitRight(s, separator, maxParts), expected);
	}

	private static <T> void assertArray(T[] actual, T... expected) {
		assertEquals(expected.length, actual.length);
		int i = 0;

		for (T item : actual) {
			assertEquals("item[" + i + "]", expected[i], item);
			++i;
		}
	}
}
