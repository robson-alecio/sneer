package sneer.bricks.software.diff.tests;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import sneer.bricks.software.diff.Diff;
import sneer.bricks.software.diff.DiffType;
import sneer.bricks.software.diff.TextComparator;
import sneer.foundation.brickness.testsupport.BrickTest;

public class TextComparatorTest extends BrickTest {
	
	private final TextComparator _subject = my(TextComparator.class);
	private final List<Object[]> _expectations = new ArrayList<Object[]>();

	@Test
	public void trivialMainDiff() throws Exception {
		
		newExpectation(DiffType.EQUAL, "abc");
		check("abc", "abc");
		
		_expectations.clear();
		newExpectation( DiffType.EQUAL, "ab"); 
		newExpectation( DiffType.INSERT, "123");
		newExpectation( DiffType.EQUAL, "c");
		check("abc", "ab123c");
		
		_expectations.clear();
		newExpectation( DiffType.EQUAL, "a"); 
		newExpectation( DiffType.DELETE, "123");
		newExpectation( DiffType.EQUAL, "bc" );
		check("a123bc", "abc");
		
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
	}

	private void newExpectation(DiffType expectedDiffType, String expectedContent) {
		_expectations.add( new Object[]{  expectedDiffType, expectedContent });
	}

	private void check(String text1, String text2) {
		Iterator<Diff> diffs = _subject.diffMain(text1, text2);

		for (Object expectation[] : _expectations) {
			Diff diff = diffs.next(); 
			assertEquals(diff.type(), expectation[0]);
			assertEquals(diff.content(), expectation[1]);
		}
		
		assertFalse(diffs.hasNext());
	}
}

