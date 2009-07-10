package sneer.bricks.software.diff.tests;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import sneer.bricks.software.diff.Diff;
import sneer.bricks.software.diff.DiffType;
import sneer.bricks.software.diff.TextComparator;
import sneer.bricks.software.diff.TextComparators;
import sneer.foundation.brickness.testsupport.BrickTest;

public class TextComparatorTest extends BrickTest {
	
	private final TextComparator _subject = my(TextComparators.class).newComparator();
	private final List<Object[]> _expectations = new ArrayList<Object[]>();

	@Test
	public void diffTest() throws Exception {

		newExpectation(DiffType.EQUAL, "abc");
		check("abc", "abc");

		_expectations.clear();
		newExpectation(DiffType.DELETE, "a");
		newExpectation(DiffType.INSERT, "b");
		check("a", "b");
		
		_expectations.clear();
		newExpectation(DiffType.EQUAL, "a");
		newExpectation(DiffType.INSERT, "123");
		newExpectation(DiffType.EQUAL, "b");
		newExpectation(DiffType.INSERT, "456");
		newExpectation(DiffType.EQUAL, "c");
		check("abc", "a123b456c");
		
		_expectations.clear();
		newExpectation(DiffType.EQUAL, "a");
		newExpectation(DiffType.DELETE, "123");
		newExpectation(DiffType.EQUAL, "b");
		newExpectation(DiffType.DELETE, "456");
		newExpectation(DiffType.EQUAL, "c");
		check("a123b456c", "abc");		
		
		_expectations.clear();
		newExpectation(DiffType.DELETE, "Apple");
		newExpectation(DiffType.INSERT, "Banana");
		newExpectation(DiffType.EQUAL, "s are a");
		newExpectation(DiffType.INSERT, "lso");
		newExpectation(DiffType.EQUAL, " fruit.");
		check("Apples are a fruit.", "Bananas are also fruit.");
		
		_expectations.clear();
		newExpectation(DiffType.DELETE, "a");
		newExpectation(DiffType.INSERT, "\u0680");
		newExpectation(DiffType.EQUAL, "x");
		newExpectation(DiffType.DELETE, "\t");
		newExpectation(DiffType.INSERT, "\000");
		check("ax\t", "\u0680x\000");
		
		_expectations.clear();
		newExpectation(DiffType.DELETE, "1");
		newExpectation(DiffType.EQUAL, "a");
		newExpectation(DiffType.DELETE, "y");
		newExpectation(DiffType.EQUAL, "b");
		newExpectation(DiffType.DELETE, "2");
		newExpectation(DiffType.INSERT, "xab");
		check("1ayb2", "abxab");
		
		_expectations.clear();
		newExpectation(DiffType.INSERT, "xaxcx");
		newExpectation(DiffType.EQUAL, "abc");
		newExpectation(DiffType.DELETE, "y");
		check("abcy", "xaxcxabc");
		
		_subject.setDualThreshold(2);
		_expectations.clear();
		newExpectation(DiffType.INSERT, "x");
		newExpectation(DiffType.EQUAL, "a");
		newExpectation(DiffType.DELETE, "b");
		newExpectation(DiffType.INSERT, "x");
		newExpectation(DiffType.EQUAL, "c");
		newExpectation(DiffType.DELETE, "y");
		newExpectation(DiffType.INSERT, "xabc");
		check("abcy", "xaxcxabc");		
		
		_expectations.clear();
		newExpectation(DiffType.DELETE, "alpha\n");
		newExpectation(DiffType.EQUAL, "beta\nalpha\n");
		newExpectation(DiffType.INSERT, "beta\n");
		check("alpha\nbeta\nalpha\n", "beta\nalpha\nbeta\n");				

		_expectations.clear();
		newExpectation(DiffType.EQUAL, "The ");
		newExpectation(DiffType.DELETE, "cat");
		newExpectation(DiffType.INSERT, "dog");
		newExpectation(DiffType.EQUAL, " in the hat.");
		check("The cat in the hat.", "The dog in the hat.");				
	
		_expectations.clear();
		newExpectation(DiffType.EQUAL, "The ");
		newExpectation(DiffType.DELETE, "cat");
		newExpectation(DiffType.INSERT, "dog");
		newExpectation(DiffType.EQUAL, " in the hat.");
		check("The cat in the hat.", "The dog in the hat.");				
	
		_expectations.clear();
		newExpectation(DiffType.EQUAL, "1111111111\n");
		newExpectation(DiffType.INSERT, "55555555\n");
		newExpectation(DiffType.EQUAL, "22222222\n" +
														   "33333333\n" +
														   "44444444\n");
		newExpectation(DiffType.DELETE, "55555555\n");
		
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
	
	private void newExpectation(DiffType expectedDiffType, String expectedContent) {
		_expectations.add( new Object[]{  expectedDiffType, expectedContent });
	}

	private void check(String text1, String text2) {
		check(_subject.diff(text1, text2));
	}

	private void check(Iterator<Diff> diffs) {
		for (Object expectation[] : _expectations) {
			Diff diff = diffs.next(); 
			assertEquals(expectation[0], diff.type());
			assertEquals(expectation[1], diff.content());
		}
		
		assertFalse(diffs.hasNext());
	}
}