package sneer.bricks.software.diff.tests;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import sneer.bricks.software.diff.TextComparator.TextBlock;
import sneer.bricks.software.diff.TextComparator.TextBlockType;
import sneer.bricks.software.diff.TextComparator;
import sneer.foundation.brickness.testsupport.BrickTest;

public class TextComparatorTest extends BrickTest {
	
	private final TextComparator _subject = my(TextComparator.class);
	private final List<Object[]> _expectations = new ArrayList<Object[]>();

	@Test
	public void diffTest() throws Exception {

		newExpectation(TextBlockType.EQUAL, "abc");
		check("abc", "abc");

		_expectations.clear();
		newExpectation(TextBlockType.DELETE, "a");
		newExpectation(TextBlockType.INSERT, "b");
		check("a", "b");
		
		_expectations.clear();
		newExpectation(TextBlockType.EQUAL, "a");
		newExpectation(TextBlockType.INSERT, "123");
		newExpectation(TextBlockType.EQUAL, "b");
		newExpectation(TextBlockType.INSERT, "456");
		newExpectation(TextBlockType.EQUAL, "c");
		check("abc", "a123b456c");
		
		_expectations.clear();
		newExpectation(TextBlockType.EQUAL, "a");
		newExpectation(TextBlockType.DELETE, "123");
		newExpectation(TextBlockType.EQUAL, "b");
		newExpectation(TextBlockType.DELETE, "456");
		newExpectation(TextBlockType.EQUAL, "c");
		check("a123b456c", "abc");		
		
		_expectations.clear();
		newExpectation(TextBlockType.DELETE, "Apple");
		newExpectation(TextBlockType.INSERT, "Banana");
		newExpectation(TextBlockType.EQUAL, "s are a");
		newExpectation(TextBlockType.INSERT, "lso");
		newExpectation(TextBlockType.EQUAL, " fruit.");
		check("Apples are a fruit.", "Bananas are also fruit.");
		
		_expectations.clear();
		newExpectation(TextBlockType.DELETE, "a");
		newExpectation(TextBlockType.INSERT, "\u0680");
		newExpectation(TextBlockType.EQUAL, "x");
		newExpectation(TextBlockType.DELETE, "\t");
		newExpectation(TextBlockType.INSERT, "\000");
		check("ax\t", "\u0680x\000");
		
		_expectations.clear();
		newExpectation(TextBlockType.DELETE, "1");
		newExpectation(TextBlockType.EQUAL, "a");
		newExpectation(TextBlockType.DELETE, "y");
		newExpectation(TextBlockType.EQUAL, "b");
		newExpectation(TextBlockType.DELETE, "2");
		newExpectation(TextBlockType.INSERT, "xab");
		check("1ayb2", "abxab");
		
		_expectations.clear();
		newExpectation(TextBlockType.INSERT, "xaxcx");
		newExpectation(TextBlockType.EQUAL, "abc");
		newExpectation(TextBlockType.DELETE, "y");
		check("abcy", "xaxcxabc");
		
		_expectations.clear();
		newExpectation(TextBlockType.INSERT, "x");
		newExpectation(TextBlockType.EQUAL, "a");
		newExpectation(TextBlockType.DELETE, "b");
		newExpectation(TextBlockType.INSERT, "x");
		newExpectation(TextBlockType.EQUAL, "c");
		newExpectation(TextBlockType.DELETE, "y");
		newExpectation(TextBlockType.INSERT, "xabc");
		check("abcy", "xaxcxabc", 2);		
		
		_expectations.clear();
		newExpectation(TextBlockType.DELETE, "alpha\n");
		newExpectation(TextBlockType.EQUAL, "beta\nalpha\n");
		newExpectation(TextBlockType.INSERT, "beta\n");
		check("alpha\nbeta\nalpha\n", "beta\nalpha\nbeta\n");				

		_expectations.clear();
		newExpectation(TextBlockType.EQUAL, "The ");
		newExpectation(TextBlockType.DELETE, "cat");
		newExpectation(TextBlockType.INSERT, "dog");
		newExpectation(TextBlockType.EQUAL, " in the hat.");
		check("The cat in the hat.", "The dog in the hat.");				
	
		_expectations.clear();
		newExpectation(TextBlockType.EQUAL, "The ");
		newExpectation(TextBlockType.DELETE, "cat");
		newExpectation(TextBlockType.INSERT, "dog");
		newExpectation(TextBlockType.EQUAL, " in the hat.");
		check("The cat in the hat.", "The dog in the hat.");				
	
		_expectations.clear();
		newExpectation(TextBlockType.EQUAL, "1111111111\n");
		newExpectation(TextBlockType.INSERT, "55555555\n");
		newExpectation(TextBlockType.EQUAL, "22222222\n" +
														   "33333333\n" +
														   "44444444\n");
		newExpectation(TextBlockType.DELETE, "55555555\n");
		
		String txt1 = 	"1111111111\n" +
							"22222222\n" +
							"33333333\n" +
							"44444444\n" +
							"55555555\n";

		String txt2 = "1111111111\n" +
							"55555555\n" +
							"22222222\n" +
							"33333333\n" +
							"44444444\n";

		check(txt1, txt2);				
	}
	
	private void newExpectation(TextBlockType expectedDiffType, String expectedContent) {
		_expectations.add( new Object[]{  expectedDiffType, expectedContent });
	}

	private void check(String text1, String text2) {
		check(_subject.diff(text1, text2));
	}

	private void check(String text1, String text2, int dualThreshold) {
		check(_subject.diff(text1, text2, dualThreshold));
	}

	private void check(Iterator<TextBlock> diffs) {
		for (Object expectation[] : _expectations) {
			TextBlock diff = diffs.next(); 
			assertEquals(expectation[0], diff.type());
			assertEquals(expectation[1], diff.content());
		}
		
		assertFalse(diffs.hasNext());
	}
}