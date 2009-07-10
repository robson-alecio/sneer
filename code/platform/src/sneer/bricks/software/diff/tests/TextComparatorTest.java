package sneer.bricks.software.diff.tests;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import sneer.bricks.software.diff.TextComparator.TextBlock;
import sneer.bricks.software.diff.TextComparator.TextBlockType;
import static sneer.bricks.software.diff.TextComparator.TextBlockType.INSERT;
import static sneer.bricks.software.diff.TextComparator.TextBlockType.DELETE;
import static sneer.bricks.software.diff.TextComparator.TextBlockType.EQUAL;
import sneer.bricks.software.diff.TextComparator;
import sneer.foundation.brickness.testsupport.BrickTest;

public class TextComparatorTest extends BrickTest {
	
	private final TextComparator _subject = my(TextComparator.class);
	private final List<Object[]> _expectations = new ArrayList<Object[]>();

	@Test
	public void diffTest() throws Exception {

		expectedBlock(EQUAL, "abc");
		check("abc", "abc");

		_expectations.clear();
		expectedBlock(DELETE, "a");
		expectedBlock(TextBlockType.INSERT, "b");
		check("a", "b");
		
		_expectations.clear();
		expectedBlock(EQUAL, "a");
		expectedBlock(INSERT, "123");
		expectedBlock(EQUAL, "b");
		expectedBlock(INSERT, "456");
		expectedBlock(EQUAL, "c");
		check("abc", "a123b456c");
		
		_expectations.clear();
		expectedBlock(EQUAL, "a");
		expectedBlock(DELETE, "123");
		expectedBlock(EQUAL, "b");
		expectedBlock(DELETE, "456");
		expectedBlock(EQUAL, "c");
		check("a123b456c", "abc");		
		
		_expectations.clear();
		expectedBlock(DELETE, "Apple");
		expectedBlock(INSERT, "Banana");
		expectedBlock(EQUAL, "s are a");
		expectedBlock(INSERT, "lso");
		expectedBlock(EQUAL, " fruit.");
		check("Apples are a fruit.", "Bananas are also fruit.");
		
		_expectations.clear();
		expectedBlock(DELETE, "a");
		expectedBlock(INSERT, "\u0680");
		expectedBlock(EQUAL, "x");
		expectedBlock(DELETE, "\t");
		expectedBlock(INSERT, "\000");
		check("ax\t", "\u0680x\000");
		
		_expectations.clear();
		expectedBlock(DELETE, "1");
		expectedBlock(EQUAL, "a");
		expectedBlock(DELETE, "y");
		expectedBlock(EQUAL, "b");
		expectedBlock(DELETE, "2");
		expectedBlock(INSERT, "xab");
		check("1ayb2", "abxab");
		
		_expectations.clear();
		expectedBlock(INSERT, "xaxcx");
		expectedBlock(EQUAL, "abc");
		expectedBlock(DELETE, "y");
		check("abcy", "xaxcxabc");
		
		_expectations.clear();
		expectedBlock(INSERT, "x");
		expectedBlock(EQUAL, "a");
		expectedBlock(DELETE, "b");
		expectedBlock(INSERT, "x");
		expectedBlock(EQUAL, "c");
		expectedBlock(DELETE, "y");
		expectedBlock(INSERT, "xabc");
		check("abcy", "xaxcxabc", 2);		
		
		_expectations.clear();
		expectedBlock(DELETE, "alpha\n");
		expectedBlock(EQUAL, "beta\nalpha\n");
		expectedBlock(INSERT, "beta\n");
		check("alpha\nbeta\nalpha\n", "beta\nalpha\nbeta\n");				

		_expectations.clear();
		expectedBlock(EQUAL, "The ");
		expectedBlock(DELETE, "cat");
		expectedBlock(INSERT, "dog");
		expectedBlock(EQUAL, " in the hat.");
		check("The cat in the hat.", "The dog in the hat.");				
	
		_expectations.clear();
		expectedBlock(EQUAL, 	"1111111111\n");
		expectedBlock(INSERT, "55555555\n");
		expectedBlock(EQUAL, 	"22222222\n" +
											"33333333\n" +
											"44444444\n");
		expectedBlock(DELETE, "55555555\n");
		
		
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
	
	private void expectedBlock(TextBlockType expectedDiffType, String expectedContent) {
		_expectations.add( new Object[]{  expectedDiffType, expectedContent });
	}

	private void check(String text1, String text2) {
		Iterator<TextBlock> iterator = _subject.diff(text1, text2);
//		System.out.println(_subject.toPrettyHtml(iterator));
		check(iterator);
	}

	private void check(String text1, String text2, int dualThreshold) {
		check(_subject.diff(text1, text2, dualThreshold));
	}

	private void check(Iterator<TextBlock> iterator) {
		for (Object expectation[] : _expectations) {
			TextBlock diff = iterator.next(); 
			assertEquals(expectation[0], diff.type());
			assertEquals(expectation[1], diff.content());
		}
		
		assertFalse(iterator.hasNext());
	}
}