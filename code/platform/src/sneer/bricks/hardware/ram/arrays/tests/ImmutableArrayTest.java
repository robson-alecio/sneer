package sneer.bricks.hardware.ram.arrays.tests;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import sneer.bricks.hardware.ram.arrays.ImmutableArray;
import sneer.bricks.hardware.ram.arrays.ImmutableArrays;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.testsupport.AssertUtils;

public class ImmutableArrayTest extends BrickTest {

	private final ImmutableArrays _subject = my(ImmutableArrays.class);

	@Test
	public void iterable() {
		
		final ImmutableArray<Integer> array = _subject.newImmutableArray(Arrays.asList(1, 2, 3));
		AssertUtils.assertSameContents(array, 1, 2, 3);
		
	}
	
	@Test
	public void immutable() {
		
		final List<Integer> original = new ArrayList<Integer>(Arrays.asList(1, 2, 3));
		final ImmutableArray<Integer> array = _subject.newImmutableArray(original);
		
		original.add(4);
		AssertUtils.assertSameContents(array, 1, 2, 3);
	}

}